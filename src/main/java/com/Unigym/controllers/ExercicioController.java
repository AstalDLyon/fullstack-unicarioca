package com.Unigym.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Unigym.entities.Exercicio;
import com.Unigym.repositories.ExercicioRepository;
import com.Unigym.repositories.TreinoRepository;

@RestController
@RequestMapping("/api/exercicios")
@CrossOrigin(origins = "*")
public class ExercicioController {

    private static final Logger log = LoggerFactory.getLogger(ExercicioController.class);
    private static final String EXERCICIOS_SUFFIX = " exercícios";

    private final ExercicioRepository exercicioRepository;
    private final TreinoRepository treinoRepository;

    public ExercicioController(ExercicioRepository exercicioRepository, TreinoRepository treinoRepository) {
        this.exercicioRepository = exercicioRepository;
        this.treinoRepository = treinoRepository;
    }
    
    // Lista geral de exercícios (GET /api/exercicios)
    @GetMapping
    public ResponseEntity<List<Exercicio>> listarTodos() {
        List<Exercicio> todos = exercicioRepository.findAll();
        if (todos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/{exercicioId}")
    public ResponseEntity<Exercicio> getExercicio(@PathVariable long exercicioId) {
        return exercicioRepository.findById(exercicioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/treino/{treinoId}") // Endpoint lista exercícios; aceita filtros opcionais
    public ResponseEntity<List<Exercicio>> getExerciciosPorTreino(
            @PathVariable long treinoId,
            @RequestParam(required = false) String diaSemana,
            @RequestParam(required = false) String grupoMuscular) {

        if (!treinoRepository.existsById(treinoId)) {
            log.warn("Treino ID {} não encontrado", treinoId);
            return ResponseEntity.notFound().build();
        }

        log.info("Início requisição exercícios treino={} diaSemana='{}' grupoMuscular='{}'", treinoId, diaSemana, grupoMuscular);
        List<Exercicio> exercicios = resolveFiltroExercicios(treinoId, diaSemana, grupoMuscular);
        log.info("Total retornado: {}{}", exercicios.size(), EXERCICIOS_SUFFIX);
        detalharDiasSemana(treinoId, exercicios, diaSemana, grupoMuscular);
        return ResponseEntity.ok(exercicios);
    }
    
    @PostMapping("/treino/{treinoId}")
    public ResponseEntity<Exercicio> adicionarExercicio(
            @PathVariable long treinoId, 
            @RequestBody Exercicio exercicio) {
        return treinoRepository.findById(treinoId)
                .map(treino -> {
                    exercicio.setTreino(treino);
                    Exercicio novoExercicio = exercicioRepository.save(exercicio);
                    return ResponseEntity.ok(novoExercicio);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Normaliza o dia da semana para o formato padrão usado no banco de dados (sem acentos)
     * @param dia Dia da semana a ser normalizado
     * @return Dia da semana normalizado
     */
    // --- Métodos auxiliares extraídos ---
    private List<Exercicio> resolveFiltroExercicios(long treinoId, String diaSemana, String grupoMuscular) {
        if (diaSemana != null && !diaSemana.isBlank()) {
            String normalizado = normalizarDiaSemana(diaSemana);
            log.debug("Dia recebido='{}' normalizado='{}'", diaSemana, normalizado);
            List<Exercicio> list = exercicioRepository.findByTreinoIdAndDiaSemana(treinoId, normalizado);
            if (list.isEmpty() && !normalizado.equals(diaSemana)) { // fallback original
                List<Exercicio> fallback = exercicioRepository.findByTreinoIdAndDiaSemana(treinoId, diaSemana);
                if (!fallback.isEmpty()) {
                    log.info("Fallback diaSemana original retornou {}{}", fallback.size(), EXERCICIOS_SUFFIX);
                    return fallback;
                }
            }
            return list;
        }
        if (grupoMuscular != null && !grupoMuscular.isBlank()) {
            return exercicioRepository.findByTreinoIdAndGrupoMuscular(treinoId, grupoMuscular);
        }
        return exercicioRepository.findByTreinoId(treinoId);
    }

    private void detalharDiasSemana(long treinoId, List<Exercicio> exercicios, String diaSemana, String grupoMuscular) {
        if (diaSemana != null || grupoMuscular != null) {
            return; // Detalhes apenas sem filtros
        }
        log.debug("Detalhando exercícios treino {}", treinoId);
        exercicios.forEach(ex -> {
            if (ex.getDiaSemana() == null || ex.getDiaSemana().isBlank()) {
                log.warn("Exercício {} ({}) sem diaSemana definido", ex.getId(), ex.getNome());
            }
        });
        var diasDistintos = exercicios.stream()
                .map(Exercicio::getDiaSemana)
                .filter(d -> d != null && !d.isBlank())
                .distinct()
                .toList();
        diasDistintos.forEach(d -> {
            long count = exercicios.stream().filter(ex -> d.equals(ex.getDiaSemana())).count();
            log.info("Dia '{}' possui {}{}", d, count, EXERCICIOS_SUFFIX);
        });
    }

    private String normalizarDiaSemana(String dia) {
        if (dia == null || dia.isBlank()) {
            log.warn("Dia da semana nulo ou vazio recebido");
            return "";
        }
        String upper = dia.trim().toUpperCase();
        return switch (upper) {
            case "SEGUNDA", "SEGUNDA-FEIRA" -> "SEGUNDA";
            case "TERÇA", "TERÇA-FEIRA", "TERCA", "TERCA-FEIRA" -> "TERCA";
            case "QUARTA", "QUARTA-FEIRA" -> "QUARTA";
            case "QUINTA", "QUINTA-FEIRA" -> "QUINTA";
            case "SEXTA", "SEXTA-FEIRA" -> "SEXTA";
            case "SÁBADO", "SABADO" -> "SABADO";
            case "DOMINGO" -> "DOMINGO";
            default -> {
                log.warn("Dia da semana não reconhecido: '{}'", upper);
                yield upper;
            }
        };
    }
}