package com.Unigym.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Unigym.entities.Aluno;
import com.Unigym.repositories.AlunoRepository;

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

    @Autowired
    private AlunoRepository alunoRepository;
    
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Login simples, obiviamente não é ideal por segurança, mas eu não manjo muito de autenticação.
        // Por simplicidade, vamos apenas verificar se o email e senha são iguais aos armazenados no banco de dados
        Aluno aluno = alunoRepository.findByEmail(loginRequest.getEmail());
        
        if (aluno != null && loginRequest.getSenha().equals(aluno.getSenha())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login realizado com sucesso");
            response.put("nome", aluno.getNome());
            response.put("id", aluno.getId());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Credenciais inválidas");
            return ResponseEntity.status(401).body(response);
        }
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