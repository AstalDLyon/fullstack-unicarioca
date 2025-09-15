package com.Unigym.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Unigym.entities.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long>{
    // Método para buscar aluno por email
    Aluno findByEmail(String email);
}
