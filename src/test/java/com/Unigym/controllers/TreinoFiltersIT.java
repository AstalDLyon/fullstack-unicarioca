package com.Unigym.controllers;

import com.Unigym.entities.Aluno;
import com.Unigym.entities.Treino;
import com.Unigym.repositories.AlunoRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
@AutoConfigureMockMvc
class TreinoFiltersIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private TreinoRepository treinoRepository;

    @Test
    @DisplayName("GET /api/treinos/aluno/{id} e /ativos retornam conforme dataFim")
    void filtrosTreinosPorAlunoEAtivos() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setNome("Filtro");
        aluno.setEmail("filtro_" + System.nanoTime() + "@uni.com");
        aluno.setSenha("123");
        Aluno savedAluno = alunoRepository.save(aluno);

        // T1 ativo (sem dataFim)
        Treino t1 = new Treino(); t1.setNome("T1"); t1.setDataInicio(LocalDate.now().minusDays(5)); t1.setAluno(savedAluno);
        // T2 ativo (dataFim futura)
        Treino t2 = new Treino(); t2.setNome("T2"); t2.setDataInicio(LocalDate.now().minusDays(2)); t2.setDataFim(LocalDate.now().plusDays(3)); t2.setAluno(savedAluno);
        // T3 inativo (dataFim passada)
        Treino t3 = new Treino(); t3.setNome("T3"); t3.setDataInicio(LocalDate.now().minusDays(10)); t3.setDataFim(LocalDate.now().minusDays(1)); t3.setAluno(savedAluno);
        treinoRepository.save(t1); treinoRepository.save(t2); treinoRepository.save(t3);

        // /aluno/{id}
        mockMvc.perform(get("/api/treinos/aluno/" + savedAluno.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // /aluno/{id}/ativos
        mockMvc.perform(get("/api/treinos/aluno/" + savedAluno.getId() + "/ativos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        var todos = treinoRepository.findByAluno(savedAluno);
        var ativos = treinoRepository.findByAlunoIdAndDataFimIsNullOrAlunoIdAndDataFimGreaterThanEqual(savedAluno.getId(), savedAluno.getId(), LocalDate.now());
        assertThat(todos).hasSize(3);
        assertThat(ativos).extracting("nome").containsExactlyInAnyOrder("T1", "T2");
    }
}
