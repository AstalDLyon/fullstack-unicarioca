package com.Unigym.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ExercicioRepository exercicioRepository;
    
    @Autowired
    private TreinoRepository treinoRepository;
    
    @GetMapping("/{exercicioId}")
    public ResponseEntity<Exercicio> getExercicio(@PathVariable Long exercicioId) {
        return exercicioRepository.findById(exercicioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/treino/{treinoId}") // Endpoint lista exercícios; aceita filtros opcionais
    public ResponseEntity<List<Exercicio>> getExerciciosPorTreino(
            @PathVariable Long treinoId,
            @RequestParam(required = false) String diaSemana,
            @RequestParam(required = false) String grupoMuscular) {
        
        // Verifica se o treino existe
        if (!treinoRepository.existsById(treinoId)) {
            System.out.println("Treino ID " + treinoId + " não encontrado");
            return ResponseEntity.notFound().build();
        }
        
    System.out.println("\n=== INÍCIO DA REQUISIÇÃO ==="); // Log abrangente para depuração
        System.out.println("Buscando exercícios para o treino ID: " + treinoId + 
                        " | Filtro diaSemana: '" + diaSemana + "'" + 
                        " | Filtro grupoMuscular: '" + grupoMuscular + "'");
        
        List<Exercicio> exercicios;
        
        // Filtra por dia da semana, se fornecido
        if (diaSemana != null && !diaSemana.isEmpty()) { // Filtro por dia
            // Normaliza dia para formato sem acento
            String diaSemanaFormatado = normalizarDiaSemana(diaSemana);
            
            System.out.println("Dia da semana recebido: '" + diaSemana + "' -> formatado para: '" + diaSemanaFormatado + "'");
            
            exercicios = exercicioRepository.findByTreinoIdAndDiaSemana(treinoId, diaSemanaFormatado);
            System.out.println("Filtrado por dia da semana: '" + diaSemanaFormatado + "' | Encontrados: " + exercicios.size() + " exercícios");
            
            // Se não encontrou exercícios e o dia foi normalizado, tente buscar com o dia original
            if (exercicios.isEmpty() && !diaSemanaFormatado.equals(diaSemana)) { // Tentativa fallback
                System.out.println("Tentando busca alternativa com o dia original: '" + diaSemana + "'");
                exercicios = exercicioRepository.findByTreinoIdAndDiaSemana(treinoId, diaSemana);
                System.out.println("Busca alternativa: Encontrados: " + exercicios.size() + " exercícios");
            }
        } 
        // Filtra por grupo muscular, se fornecido
    else if (grupoMuscular != null && !grupoMuscular.isEmpty()) { // Filtro alternativo
            exercicios = exercicioRepository.findByTreinoIdAndGrupoMuscular(treinoId, grupoMuscular);
            System.out.println("Filtrado por grupo muscular: '" + grupoMuscular + "' | Encontrados: " + exercicios.size() + " exercícios");
        } 
        // Caso contrário, retorna todos os exercícios do treino
        else {
            exercicios = exercicioRepository.findByTreinoId(treinoId);
            System.out.println("Sem filtro | Encontrados: " + exercicios.size() + " exercícios");
            
            // Imprimir informações detalhadas sobre cada exercício
            System.out.println("\n=== DETALHES DOS EXERCÍCIOS PARA O TREINO ID " + treinoId + " ===");
            for (Exercicio ex : exercicios) { // Log detalhado linha a linha
                System.out.println("Exercício ID: " + ex.getId() + 
                                    " | Nome: '" + ex.getNome() + "'" + 
                                    " | Dia: '" + ex.getDiaSemana() + "'" +
                                    " | Grupo: '" + ex.getGrupoMuscular() + "'");
                
                if (ex.getDiaSemana() == null || ex.getDiaSemana().isEmpty()) {
                    System.out.println("AVISO: Exercício ID " + ex.getId() + " (" + ex.getNome() + ") não tem dia da semana definido");
                }
            }
            
            // Extrair e imprimir todos os dias da semana distintos
            List<String> diasSemana = exercicios.stream() // Extrai dias distintos
                .map(Exercicio::getDiaSemana)
                .filter(dia -> dia != null && !dia.isEmpty())
                .distinct()
                .collect(java.util.stream.Collectors.toList());
            
            System.out.println("\nDias da semana encontrados neste treino: " + diasSemana);
            
            // Para cada dia encontrado, mostrar quantos exercícios existem
            for (String dia : diasSemana) {
                long count = exercicios.stream()
                    .filter(ex -> dia.equals(ex.getDiaSemana()))
                    .count();
                System.out.println("- Dia '" + dia + "': " + count + " exercícios");
            }
        }
        
        System.out.println("=== FIM DA REQUISIÇÃO ===\n");
        return ResponseEntity.ok(exercicios);
    }
    
    @PostMapping("/treino/{treinoId}")
    public ResponseEntity<Exercicio> adicionarExercicio(
            @PathVariable Long treinoId, 
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
    private String normalizarDiaSemana(String dia) { // Centraliza regras de equivalência de dias
        if (dia == null || dia.trim().isEmpty()) {
            System.out.println("AVISO: Dia da semana nulo ou vazio recebido na normalização");
            return "";
        }
        
        // Padronizar para maiúsculas e remover espaços extras
        String diaUpper = dia.trim().toUpperCase();
        
        // Mapeamento de dias com acentos para sem acentos
        // Note que no import.sql, os dias são sempre sem acentos: SEGUNDA, TERCA, QUARTA, etc.
        switch (diaUpper) {
            case "SEGUNDA": 
            case "SEGUNDA-FEIRA": 
                return "SEGUNDA";
                
            case "TERÇA": 
            case "TERÇA-FEIRA":
            case "TERCA": 
            case "TERCA-FEIRA": 
                return "TERCA";
                
            case "QUARTA": 
            case "QUARTA-FEIRA": 
                return "QUARTA";
                
            case "QUINTA": 
            case "QUINTA-FEIRA": 
                return "QUINTA";
                
            case "SEXTA": 
            case "SEXTA-FEIRA": 
                return "SEXTA";
                
            case "SÁBADO":
            case "SABADO": 
                return "SABADO";
                
            case "DOMINGO": 
                return "DOMINGO";
                
            default:
                System.out.println("AVISO: Dia da semana não reconhecido: '" + diaUpper + "'");
                return diaUpper; // Se não reconhecer, retorna o valor original em maiúsculas
        }
    }
}