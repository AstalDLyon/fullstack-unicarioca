package com.Unigym.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.Unigym.entities.Instrutor;
import com.Unigym.repositories.InstrutorRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/instrutores") // se eu acessar o localhost:8080/instrutores estarei acessando aqui
public class InstrutorController {

    private final InstrutorRepository repository;

    public InstrutorController(InstrutorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Instrutor> getAllInstrutores() {
        return repository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Instrutor getInstrutorById(@PathVariable long id) {
        return repository.findById(id).orElse(null);
    }
    
    @PostMapping
    public Instrutor insert(@RequestBody Instrutor instrutor) {
        if (instrutor == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Instrutor inválido");
        }
        return repository.save(instrutor);
    }

}
