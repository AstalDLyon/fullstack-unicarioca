package com.Unigym.controllers;

import com.Unigym.entities.Aluno;
import com.Unigym.entities.Medida;
import com.Unigym.repositories.AlunoRepository;
import com.Unigym.repositories.MedidaRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
@AutoConfigureMockMvc
class MedidaControllerIT {

@Autowired
private MockMvc mockMvc;
@Autowired
private AlunoRepository alunoRepository;
@Autowired
private MedidaRepository medidaRepository;

@Test
@DisplayName("Medidas por aluno ordenadas desc, resumo contém campos esperados e ultima retorna mais recente")
void medidasOrderingResumoUltima() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setNome("Medidas");
        aluno.setEmail("med_" + System.nanoTime() + "@uni.com");
        aluno.setSenha("123");
        Aluno savedAluno = alunoRepository.save(aluno);

        // três medidas em datas diferentes
        Medida m1 = new Medida(); m1.setAluno(savedAluno); m1.setData(LocalDate.now().minusDays(2)); m1.setPeso(80.0); m1.setAltura(1.80); m1.setPercentualGordura(20.0);
        Medida m2 = new Medida(); m2.setAluno(savedAluno); m2.setData(LocalDate.now()); m2.setPeso(81.0); m2.setAltura(1.80); m2.setPercentualGordura(19.5);
        Medida m3 = new Medida(); m3.setAluno(savedAluno); m3.setData(LocalDate.now().minusDays(1)); m3.setPeso(79.5); m3.setAltura(1.80); m3.setPercentualGordura(20.5);
        medidaRepository.saveAll(requireNonNull(List.of(m1, m2, m3)));

        // endpoints
        mockMvc.perform(get("/api/medidas/aluno/" + savedAluno.getId()).contentType(requireNonNull(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/medidas/aluno/" + savedAluno.getId() + "/ultima").contentType(requireNonNull(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/medidas/aluno/" + savedAluno.getId() + "/resumo").contentType(requireNonNull(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());

        var lista = medidaRepository.findByAlunoIdOrderByDataDesc(savedAluno.getId());
        assertThat(lista).extracting("data", LocalDate.class)
                .isSortedAccordingTo((a, b) -> b.compareTo(a)); // desc
        var ultima = medidaRepository.findFirstByAlunoIdOrderByDataDesc(savedAluno.getId());
        assertThat(ultima.getData()).isEqualTo(LocalDate.now());
        var resumo = medidaRepository.findMedidasResumidasByAlunoId(savedAluno.getId());
        assertThat(resumo).isNotEmpty();
}
}
