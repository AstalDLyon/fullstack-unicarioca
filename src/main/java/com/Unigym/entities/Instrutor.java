package com.Unigym.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "Instrutores")
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // definindo id como chave primaria
    private Long id;
    private String nome;
    
    @Column(unique = true)
    private String email;
    
    private String senha;

    public Instrutor() {
        // Construtor padrão
    }

    // getters e setters
    // sem um setter pra Id, já que o banco de dados vai gerar o mesmo, sem falar que não achei seguro o Id ser acessivel
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
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
