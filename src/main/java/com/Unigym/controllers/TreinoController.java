package com.Unigym.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Unigym.entities.Aluno;
import com.Unigym.entities.Treino;
import com.Unigym.repositories.AlunoRepository;
import com.Unigym.repositories.TreinoRepository;

@RestController
@RequestMapping("/api/treinos")
@CrossOrigin(origins = "*")
public class TreinoController {

    @Autowired
    private TreinoRepository treinoRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @GetMapping("/{treinoId}")
    public ResponseEntity<Treino> getTreino(@PathVariable Long treinoId) {
        return treinoRepository.findById(treinoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<Treino>> getTreinosDoAluno(@PathVariable Long alunoId) {
        try {
            System.out.println("Buscando todos os treinos para o aluno ID: " + alunoId);
            
            Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
            if (aluno == null) {
                System.out.println("Aluno não encontrado com ID: " + alunoId);
                return ResponseEntity.notFound().build();
            }
            
            List<Treino> treinos = treinoRepository.findByAluno(aluno);
            System.out.println("Total de treinos encontrados: " + treinos.size());
            
            return ResponseEntity.ok(treinos);
        } catch (Exception e) {
            System.err.println("Erro ao buscar todos os treinos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/aluno/{alunoId}/ativos")
    public ResponseEntity<List<Treino>> getTreinosAtivosDoAluno(@PathVariable Long alunoId) {
        try {
            System.out.println("Buscando treinos ativos para o aluno ID: " + alunoId);
            
            Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
            if (aluno == null) {
                System.out.println("Aluno não encontrado com ID: " + alunoId);
                return ResponseEntity.notFound().build();
            }
            
            LocalDate hoje = LocalDate.now();
            System.out.println("Data atual: " + hoje);
            
            List<Treino> treinos = treinoRepository.findByAlunoIdAndDataFimIsNullOrAlunoIdAndDataFimGreaterThanEqual(alunoId, alunoId, hoje);
            System.out.println("Treinos encontrados: " + treinos.size());
            
            return ResponseEntity.ok(treinos);
        } catch (Exception e) {
            System.err.println("Erro ao buscar treinos ativos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Treino> criarTreino(@RequestBody Treino treino) {
        Treino novoTreino = treinoRepository.save(treino);
        return ResponseEntity.ok(novoTreino);
    }
}