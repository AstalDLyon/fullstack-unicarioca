package com.Unigym.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Unigym.entities.Aluno;
import com.Unigym.entities.Treino;

public interface TreinoRepository extends JpaRepository<Treino, Long> {
    // Encontra todos os treinos de um aluno
    List<Treino> findByAluno(Aluno aluno);
    
    // Encontra todos os treinos ativos de um aluno (treinos sem data de fim ou com data de fim futura)
    List<Treino> findByAlunoIdAndDataFimIsNullOrAlunoIdAndDataFimGreaterThanEqual(Long alunoId1, Long alunoId2, java.time.LocalDate hoje);
}