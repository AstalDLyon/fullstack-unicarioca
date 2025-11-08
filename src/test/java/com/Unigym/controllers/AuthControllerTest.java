package com.Unigym.controllers;

import com.Unigym.entities.Aluno;
import com.Unigym.entities.Instrutor;
import com.Unigym.repositories.AlunoRepository;
import com.Unigym.repositories.InstrutorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @TestConfiguration
    static class MocksConfig {
        @Bean
        AlunoRepository alunoRepository() { return Mockito.mock(AlunoRepository.class); }
        @Bean
        InstrutorRepository instrutorRepository() { return Mockito.mock(InstrutorRepository.class); }
    }

    @Test
    @DisplayName("Login aluno sucesso retorna role ALUNO e redirect")
    void loginAlunoSuccess() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setNome("Joao");
        aluno.setEmail("joao@uni.com");
        aluno.setSenha("123");
        // id via reflexão simples
        var idField = Aluno.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(aluno, 10L);

        Mockito.when(alunoRepository.findByEmail("joao@uni.com")).thenReturn(aluno);

        mockMvc.perform(post("/api/auth/login")
                .contentType(requireNonNull(MediaType.APPLICATION_JSON))
                .content("{\"email\":\"joao@uni.com\",\"senha\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.role").value("ALUNO"))
                .andExpect(jsonPath("$.redirect").value("/meus-treinos.html"));
    }

    @Test
    @DisplayName("Login instrutor sucesso retorna role INSTRUTOR e redirect")
    void loginInstrutorSuccess() throws Exception {
        Instrutor instrutor = new Instrutor();
        instrutor.setNome("Maria");
        instrutor.setEmail("maria@uni.com");
        instrutor.setSenha("abc");
        var idField = Instrutor.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(instrutor, 5L);

        Mockito.when(alunoRepository.findByEmail("maria@uni.com")).thenReturn(null);
        Mockito.when(instrutorRepository.findByEmail("maria@uni.com")).thenReturn(instrutor);

        mockMvc.perform(post("/api/auth/login")
                .contentType(requireNonNull(MediaType.APPLICATION_JSON))
                .content("{\"email\":\"maria@uni.com\",\"senha\":\"abc\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.role").value("INSTRUTOR"))
                .andExpect(jsonPath("$.redirect").value("/area-instrutor.html"));
    }

    @Test
    @DisplayName("Credenciais inválidas retornam 401 e success=false")
    void loginFailure() throws Exception {
        Mockito.when(alunoRepository.findByEmail("x@uni.com")).thenReturn(null);
        Mockito.when(instrutorRepository.findByEmail("x@uni.com")).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                .contentType(requireNonNull(MediaType.APPLICATION_JSON))
                .content("{\"email\":\"x@uni.com\",\"senha\":\"zzz\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }
}
