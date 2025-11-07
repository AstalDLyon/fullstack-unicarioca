package com.Unigym.controllers;

import com.Unigym.entities.Aluno;
import com.Unigym.entities.Exercicio;
import com.Unigym.entities.Medida;
import com.Unigym.entities.Treino;
import com.Unigym.repositories.AlunoRepository;
import com.Unigym.repositories.ExercicioRepository;
import com.Unigym.repositories.MedidaRepository;
import com.Unigym.repositories.TreinoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
@AutoConfigureMockMvc
class AlunoDeletionIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private TreinoRepository treinoRepository;
    @Autowired
    private ExercicioRepository exercicioRepository;
    @Autowired
    private MedidaRepository medidaRepository;

    @Test
    @DisplayName("DELETE /alunos/{id} remove aluno, medidas, treinos e exercicios")
    void deleteAlunoCascade() throws Exception {
        // Arrange - cria aluno
    Aluno aluno = new Aluno();
    aluno.setNome("Aluno Cascade");
    aluno.setEmail("cascade_" + System.nanoTime() + "@uni.com");
    aluno.setSenha("123");
    Aluno savedAluno = alunoRepository.save(aluno);

        // Medida
        Medida m = new Medida();
    m.setAluno(savedAluno);
        m.setData(LocalDate.now());
        m.setPeso(80.0);
        m.setAltura(1.80);
        medidaRepository.save(m);

        // Treino + exercicio
    Treino t = new Treino();
        t.setNome("Treino A");
        t.setDescricao("Desc");
        t.setDataInicio(LocalDate.now());
    t.setAluno(savedAluno);
    Treino savedTreino = treinoRepository.save(t);

        Exercicio e = new Exercicio();
        e.setNome("Supino");
        e.setGrupoMuscular("PEITO");
        e.setSeries(3);
        e.setRepeticoes(10);
        e.setDiaSemana("SEGUNDA");
    e.setTreino(savedTreino);
        exercicioRepository.save(e);

        // Sanity pre-delete
    final Long alunoId = savedAluno.getId();
    final Long treinoId = savedTreino.getId();
    assertThat(alunoRepository.findById(alunoId)).isPresent();
    assertThat(medidaRepository.findByAlunoId(alunoId)).hasSize(1);
    assertThat(treinoRepository.findByAluno(savedAluno)).hasSize(1);
    assertThat(exercicioRepository.findAll()).anyMatch(x -> x.getTreino().getId().equals(treinoId));

        // Act - delete
    mockMvc.perform(delete("/alunos/" + alunoId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Assert - tudo removido
    assertThat(alunoRepository.findById(alunoId)).isNotPresent();
    assertThat(medidaRepository.findByAlunoId(alunoId)).isEmpty();
    assertThat(treinoRepository.findByAluno(savedAluno)).isEmpty();
    assertThat(exercicioRepository.findAll())
        .extracting(e2 -> e2.getTreino() != null && e2.getTreino().getAluno() != null ? e2.getTreino().getAluno().getId() : null)
        .doesNotContain(alunoId);
    }
}
