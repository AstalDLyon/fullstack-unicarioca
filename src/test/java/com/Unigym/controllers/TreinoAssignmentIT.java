package com.Unigym.controllers;

import com.Unigym.entities.Aluno;
import com.Unigym.entities.Exercicio;
import com.Unigym.entities.Treino;
import com.Unigym.repositories.AlunoRepository;
import com.Unigym.repositories.ExercicioRepository;
import com.Unigym.repositories.TreinoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
@AutoConfigureMockMvc
class TreinoAssignmentIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private TreinoRepository treinoRepository;
    @Autowired
    private ExercicioRepository exercicioRepository;

    @Test
    @DisplayName("POST /api/treinos/{treinoId}/atribuir/{alunoId} clona treino e exercicios para o aluno")
    void atribuirClonaTreinoEExercicios() throws Exception {
        // Aluno alvo
        Aluno aluno = new Aluno();
        aluno.setNome("Alvo");
        aluno.setEmail("alvo_" + System.nanoTime() + "@uni.com");
        aluno.setSenha("123");
        Aluno savedAluno = alunoRepository.save(aluno);

        // Treino base
        Treino base = new Treino();
        base.setNome("Força A");
        base.setDescricao("bloco A");
        base.setDataInicio(LocalDate.now().minusDays(10));
        Treino savedBase = treinoRepository.save(base);

        // Exercicios do treino base
        Exercicio e1 = new Exercicio();
        e1.setNome("Supino"); e1.setGrupoMuscular("PEITO"); e1.setSeries(3); e1.setRepeticoes(10); e1.setDiaSemana("SEGUNDA"); e1.setTreino(savedBase);
        Exercicio e2 = new Exercicio();
        e2.setNome("Agachamento"); e2.setGrupoMuscular("PERNAS"); e2.setSeries(4); e2.setRepeticoes(8); e2.setDiaSemana("QUARTA"); e2.setTreino(savedBase);
        exercicioRepository.saveAll(requireNonNull(List.of(e1, e2)));

        // Act: atribui
        mockMvc.perform(post("/api/treinos/" + savedBase.getId() + "/atribuir/" + savedAluno.getId())
        .contentType(requireNonNull(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        // Assert: novo treino do aluno, diferente do base
        var treinosDoAluno = treinoRepository.findByAluno(savedAluno);
        assertThat(treinosDoAluno).hasSize(1);
        Treino clonado = treinosDoAluno.get(0);
        assertThat(clonado.getId()).isNotEqualTo(savedBase.getId());
        assertThat(clonado.getAluno().getId()).isEqualTo(savedAluno.getId());
        var exerciciosClonados = exercicioRepository.findByTreinoId(clonado.getId());
        assertThat(exerciciosClonados)
            .hasSize(2)
            .allMatch(x -> x.getTreino().getId().equals(clonado.getId()));
    }
}
