package com.Unigym.controllers; 
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(MedidaController.class);

    private final MedidaRepository medidaRepository;
    private final AlunoRepository alunoRepository;

    public MedidaController(MedidaRepository medidaRepository, AlunoRepository alunoRepository) {
        this.medidaRepository = medidaRepository;
        this.alunoRepository = alunoRepository;
    }
    
    // Lista geral de medidas (GET /api/medidas)
    @GetMapping
    public ResponseEntity<List<Medida>> listarTodas() {
        List<Medida> todas = medidaRepository.findAll();
        if (todas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(todas);
    }
    
    @GetMapping("/{medidaId}")
    public ResponseEntity<Medida> getMedida(@PathVariable long medidaId) {
        return medidaRepository.findById(medidaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<Medida>> getMedidasDoAluno(@PathVariable long alunoId) {
        try {
            log.info("Buscando medidas para o aluno ID: {}", alunoId);
            
            Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
            if (aluno == null) {
                log.warn("Aluno não encontrado com ID: {}", alunoId);
                return ResponseEntity.notFound().build();
            }
            
            List<Medida> medidas = medidaRepository.findByAlunoIdOrderByDataDesc(alunoId);
            log.info("Medidas encontradas: {}", medidas.size());
            
            return ResponseEntity.ok(medidas);
        } catch (Exception e) {
            log.error("Erro ao buscar medidas", e);
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/aluno/{alunoId}/periodo")
    public ResponseEntity<List<Medida>> getMedidasPorPeriodo(
            @PathVariable long alunoId,
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
    public ResponseEntity<Medida> getUltimaMedida(@PathVariable long alunoId) {
        try {
            log.info("Buscando última medida para o aluno ID: {}", alunoId);
            
            Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
            if (aluno == null) {
                log.warn("Aluno não encontrado com ID: {}", alunoId);
                return ResponseEntity.notFound().build();
            }
            
            Medida ultimaMedida = medidaRepository.findFirstByAlunoIdOrderByDataDesc(alunoId);
            if (ultimaMedida == null) {
                log.info("Nenhuma medida encontrada para o aluno ID: {}", alunoId);
                return ResponseEntity.notFound().build();
            }
            
            log.debug("Última medida encontrada id={} data={}", ultimaMedida.getId(), ultimaMedida.getData());
            return ResponseEntity.ok(ultimaMedida);
        } catch (Exception e) {
            log.error("Erro ao buscar última medida", e);
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/aluno/{alunoId}/resumo")
    public ResponseEntity<List<Object>> getResumoMedidas(@PathVariable long alunoId) {
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
        if (medida.getAluno() == null) {
            return ResponseEntity.badRequest().build();
        }
        Long alunoIdObj = medida.getAluno().getId();
        if (alunoIdObj == null) {
            return ResponseEntity.badRequest().build();
        }
        long alunoId = alunoIdObj; // converte para primitivo para satisfazer análise de null-safety
        var alunoOpt = alunoRepository.findById(alunoId);
        if (alunoOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        medida.setAluno(alunoOpt.get());
        
        // Define a data atual se não for fornecida
        if (medida.getData() == null) {
            medida.setData(LocalDate.now());
        }
        
        Medida novaMedida = medidaRepository.save(medida);
        return ResponseEntity.ok(novaMedida);
    }
}