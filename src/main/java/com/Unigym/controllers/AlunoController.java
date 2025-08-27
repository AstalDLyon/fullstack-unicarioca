package com.Unigym.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Unigym.entities.Aluno;
import com.Unigym.repositories.AlunoRepository;

@RestController
@RequestMapping(value = "/alunos") // se eu acessar o localhost:8080/alunos estarei acessando aqui
public class AlunoController {

    @Autowired
    private AlunoRepository repository;

    @GetMapping
    public List<Aluno> getAllAlunos() {
        return repository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Aluno getAlunoById(@PathVariable Long id) {
        Aluno result = repository.findById(id).orElse(null);
        return result;
    }
    
    @PostMapping
    public Aluno insert(@RequestBody Aluno aluno) {
        Aluno result = repository.save(aluno);
        return result;
    }
    
}
