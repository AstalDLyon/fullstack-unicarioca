package com.Unigym.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Unigym.entities.Aluno;
import com.Unigym.entities.Instrutor;
import com.Unigym.repositories.AlunoRepository;
import com.Unigym.repositories.InstrutorRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthController
 * 
 * Este controlador gerencia a autenticação de usuários no sistema UniGym,
 * fornecendo endpoints para login, validação de tokens e gerenciamento de sessões.
 * 
 * Endpoint base: /api/auth
 * 
 * Funcionalidades:
 * - Login de alunos via email e senha
 * - Geração de resposta com dados do usuário autenticado
 * - Validação de credenciais
 * 
 * Observações de segurança:
 * - Implementação atual é simplificada para fins educacionais
 * - Em ambiente de produção, deveria incluir:
 *   - Criptografia de senha com algoritmos como BCrypt
 *   - Implementação de JWT ou similar para gestão de tokens
 *   - Proteção contra ataques de força bruta
 *   - HTTPS para todas as comunicações
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permite requisições de qualquer origem
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_MESSAGE = "message";
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;

    public AuthController(AlunoRepository alunoRepository, InstrutorRepository instrutorRepository) {
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
    }
    
    /**
     * Realiza a autenticação do usuário no sistema
     * 
     * Método HTTP: POST
     * URL: /api/auth/login
     * Body: {email, senha}
     * 
     * @param loginRequest Objeto contendo email e senha do usuário
     * @return ResponseEntity com status 200 e dados do usuário se autenticação bem-sucedida,
     *         ou status 401 com mensagem de erro caso contrário
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getSenha() == null) {
            log.warn("Requisição de login inválida: dados incompletos");
            return ResponseEntity.badRequest().body(Map.of(KEY_SUCCESS, false, KEY_MESSAGE, "Dados de login incompletos"));
        }
        // Login simples, obiviamente não é ideal por segurança, mas eu não manjo muito de autenticação.
        // Por simplicidade, vamos apenas verificar se o email e senha são iguais aos armazenados no banco de dados
        // Tenta autenticar como Aluno
        Aluno aluno = alunoRepository.findByEmail(loginRequest.getEmail());
        if (aluno != null && loginRequest.getSenha().equals(aluno.getSenha())) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            response.put(KEY_MESSAGE, "Login realizado com sucesso");
            response.put("nome", aluno.getNome());
            response.put("id", aluno.getId());
            response.put("role", "ALUNO");
            response.put("redirect", "/meus-treinos.html");
            log.info("Login aluno bem-sucedido: id={} email={}", aluno.getId(), aluno.getEmail());
            return ResponseEntity.ok(response);
        }

        // Tenta autenticar como Instrutor
        Instrutor instrutor = instrutorRepository.findByEmail(loginRequest.getEmail());
        if (instrutor != null && loginRequest.getSenha().equals(instrutor.getSenha())) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            response.put(KEY_MESSAGE, "Login realizado com sucesso");
            response.put("nome", instrutor.getNome());
            response.put("id", instrutor.getId());
            response.put("role", "INSTRUTOR");
            response.put("redirect", "/area-instrutor.html");
            log.info("Login instrutor bem-sucedido: id={} email={}", instrutor.getId(), instrutor.getEmail());
            return ResponseEntity.ok(response);
        }

        Map<String, Object> response = new HashMap<>();
        response.put(KEY_SUCCESS, false);
        response.put(KEY_MESSAGE, "Credenciais inválidas");
        log.warn("Falha de login para email={}", loginRequest.getEmail());
        return ResponseEntity.status(401).body(response);
    }
    
    /**
     * Classe interna que representa a estrutura de uma requisição de login
     * 
     * Esta classe é usada para mapear o JSON recebido na requisição para
     * um objeto Java através da desserialização automática do Spring.
     */
    public static class LoginRequest {
        private String email;
        private String senha;
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getSenha() {
            return senha;
        }
        
        public void setSenha(String senha) {
            this.senha = senha;
        }
    }
}