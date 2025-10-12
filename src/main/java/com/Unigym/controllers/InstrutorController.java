package com.Unigym.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Unigym.entities.Instrutor;
import com.Unigym.repositories.InstrutorRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/instrutores") //// se eu acessar o localhost:8080/instrutores estarei acessando aqui
public class InstrutorController {

    @Autowired
    private InstrutorRepository repository;

    @GetMapping
    public List<Instrutor> getAllInstrutores() {
        return repository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Instrutor getInstrutorById(@PathVariable Long id) {
        Instrutor result = repository.findById(id).orElse(null);
        return result;
    }
    
    @PostMapping
    public Instrutor insert(@RequestBody Instrutor instrutor) {
        Instrutor result = repository.save(instrutor);
        return result;
    }

}
