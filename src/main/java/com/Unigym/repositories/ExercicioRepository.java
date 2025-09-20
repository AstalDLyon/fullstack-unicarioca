package com.Unigym.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Unigym.entities.Exercicio;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {
    // Encontra todos os exercícios de um treino
    List<Exercicio> findByTreinoId(Long treinoId);
    
    // Encontra exercícios por dia da semana em um treino específico
    List<Exercicio> findByTreinoIdAndDiaSemana(Long treinoId, String diaSemana);
    
    // Encontra exercícios por grupo muscular em um treino específico
    List<Exercicio> findByTreinoIdAndGrupoMuscular(Long treinoId, String grupoMuscular);
}