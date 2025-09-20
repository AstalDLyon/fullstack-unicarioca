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
    
    @GetMapping("/treino/{treinoId}")
    public ResponseEntity<List<Exercicio>> getExerciciosPorTreino(
            @PathVariable Long treinoId,
            @RequestParam(required = false) String diaSemana,
            @RequestParam(required = false) String grupoMuscular) {
        
        // Verifica se o treino existe
        if (!treinoRepository.existsById(treinoId)) {
            return ResponseEntity.notFound().build();
        }
        
        List<Exercicio> exercicios;
        
        // Filtra por dia da semana, se fornecido
        if (diaSemana != null && !diaSemana.isEmpty()) {
            exercicios = exercicioRepository.findByTreinoIdAndDiaSemana(treinoId, diaSemana);
        } 
        // Filtra por grupo muscular, se fornecido
        else if (grupoMuscular != null && !grupoMuscular.isEmpty()) {
            exercicios = exercicioRepository.findByTreinoIdAndGrupoMuscular(treinoId, grupoMuscular);
        } 
        // Caso contrário, retorna todos os exercícios do treino
        else {
            exercicios = exercicioRepository.findByTreinoId(treinoId);
        }
        
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
}