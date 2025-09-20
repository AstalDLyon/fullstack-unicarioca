package com.Unigym.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Unigym.entities.Aluno;
import com.Unigym.entities.Medida;
import com.Unigym.repositories.AlunoRepository;
import com.Unigym.repositories.MedidaRepository;

@RestController
@RequestMapping("/api/medidas")
@CrossOrigin(origins = "*")
public class MedidaController {

    @Autowired
    private MedidaRepository medidaRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @GetMapping("/{medidaId}")
    public ResponseEntity<Medida> getMedida(@PathVariable Long medidaId) {
        return medidaRepository.findById(medidaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<Medida>> getMedidasDoAluno(@PathVariable Long alunoId) {
        try {
            System.out.println("Buscando medidas para o aluno ID: " + alunoId);
            
            Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
            if (aluno == null) {
                System.out.println("Aluno não encontrado com ID: " + alunoId);
                return ResponseEntity.notFound().build();
            }
            
            List<Medida> medidas = medidaRepository.findByAlunoIdOrderByDataDesc(alunoId);
            System.out.println("Medidas encontradas: " + medidas.size());
            
            return ResponseEntity.ok(medidas);
        } catch (Exception e) {
            System.err.println("Erro ao buscar medidas: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/aluno/{alunoId}/periodo")
    public ResponseEntity<List<Medida>> getMedidasPorPeriodo(
            @PathVariable Long alunoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Medida> medidas = medidaRepository.findByAlunoIdAndDataBetweenOrderByDataDesc(alunoId, inicio, fim);
        return ResponseEntity.ok(medidas);
    }
    
    @GetMapping("/aluno/{alunoId}/ultima")
    public ResponseEntity<Medida> getUltimaMedida(@PathVariable Long alunoId) {
        try {
            System.out.println("Buscando última medida para o aluno ID: " + alunoId);
            
            Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
            if (aluno == null) {
                System.out.println("Aluno não encontrado com ID: " + alunoId);
                return ResponseEntity.notFound().build();
            }
            
            Medida ultimaMedida = medidaRepository.findFirstByAlunoIdOrderByDataDesc(alunoId);
            if (ultimaMedida == null) {
                System.out.println("Nenhuma medida encontrada para o aluno ID: " + alunoId);
                return ResponseEntity.notFound().build();
            }
            
            System.out.println("Última medida encontrada com ID: " + ultimaMedida.getId() + ", data: " + ultimaMedida.getData());
            return ResponseEntity.ok(ultimaMedida);
        } catch (Exception e) {
            System.err.println("Erro ao buscar última medida: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/aluno/{alunoId}/resumo")
    public ResponseEntity<List<Object>> getResumoMedidas(@PathVariable Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Object> resumo = medidaRepository.findMedidasResumidasByAlunoId(alunoId);
        return ResponseEntity.ok(resumo);
    }
    
    @PostMapping
    public ResponseEntity<Medida> criarMedida(@RequestBody Medida medida) {
        // Verifica se o aluno existe
        if (medida.getAluno() != null && medida.getAluno().getId() != null) {
            Aluno aluno = alunoRepository.findById(medida.getAluno().getId()).orElse(null);
            if (aluno == null) {
                return ResponseEntity.badRequest().build();
            }
            medida.setAluno(aluno);
        } else {
            return ResponseEntity.badRequest().build();
        }
        
        // Define a data atual se não for fornecida
        if (medida.getData() == null) {
            medida.setData(LocalDate.now());
        }
        
        Medida novaMedida = medidaRepository.save(medida);
        return ResponseEntity.ok(novaMedida);
    }
}