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

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permite requisições de qualquer origem
public class AuthController {

    @Autowired
    private AlunoRepository alunoRepository;
    
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
    
    // Classe interna para representar a requisição de login
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