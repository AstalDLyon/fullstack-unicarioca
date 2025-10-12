package com.Unigym.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.Unigym.entities.Exercicio;
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

    // Lista todos os treinos existentes (para seleção na área do instrutor)
    @GetMapping
    public ResponseEntity<List<Treino>> listarTodos() {
        List<Treino> treinos = treinoRepository.findAll();
        return ResponseEntity.ok(treinos);
    }
    
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

    // Atribui (clona) um treino existente para um aluno específico
    // Cria um novo Treino copiando os dados do original e replicando os exercícios
    @PostMapping("/{treinoId}/atribuir/{alunoId}")
    public ResponseEntity<Treino> atribuirTreinoParaAluno(@PathVariable Long treinoId, @PathVariable Long alunoId) {
        try {
            var treinoOriginalOpt = treinoRepository.findById(treinoId);
            if (treinoOriginalOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var alunoOpt = alunoRepository.findById(alunoId);
            if (alunoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Treino treinoOriginal = treinoOriginalOpt.get();
            Aluno aluno = alunoOpt.get();

            // Novo treino baseado no original
            Treino novo = new Treino();
            novo.setNome(treinoOriginal.getNome());
            novo.setDescricao(treinoOriginal.getDescricao());
            novo.setDataInicio(LocalDate.now());
            novo.setDataFim(null);
            novo.setAluno(aluno);
            novo.setInstrutor(treinoOriginal.getInstrutor()); // mantém instrutor do treino original

            // Clona os exercícios
            List<Exercicio> exerciciosClonados = new ArrayList<>();
            if (treinoOriginal.getExercicios() != null) {
                for (Exercicio e : treinoOriginal.getExercicios()) {
                    Exercicio clone = new Exercicio();
                    clone.setNome(e.getNome());
                    clone.setGrupoMuscular(e.getGrupoMuscular());
                    clone.setSeries(e.getSeries());
                    clone.setRepeticoes(e.getRepeticoes());
                    clone.setCarga(e.getCarga());
                    clone.setObservacoes(e.getObservacoes());
                    clone.setDiaSemana(e.getDiaSemana());
                    // Relaciona ao novo treino
                    clone.setTreino(novo);
                    exerciciosClonados.add(clone);
                }
            }
            novo.setExercicios(exerciciosClonados);

            Treino salvo = treinoRepository.save(novo); // Cascade.ALL persiste os exercícios
            return ResponseEntity.ok(salvo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}