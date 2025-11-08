package com.Unigym.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

/**
 * Aluno
 * 
 * Esta classe representa a entidade Aluno no sistema UniGym, armazenando todas as
 * informações relevantes sobre os alunos da academia, incluindo dados pessoais
 * e relacionamentos com outras entidades.
 * 
 * Relacionamentos:
 * - ManyToOne com Instrutor: Cada aluno está associado a um instrutor específico
 * - OneToMany com Treino: Um aluno pode ter múltiplos treinos associados a ele
 * - OneToMany com Medida: Um aluno pode ter múltiplos registros de medidas corporais
 * 
 * Anotações importantes:
 * - @JsonIdentityInfo: Previne recursão infinita durante a serialização JSON das relações
 * - @JsonIgnoreProperties: Evita que campos específicos sejam serializados para JSON
 * - @Entity: Define como uma entidade JPA mapeada para banco de dados
 * - @Table: Define o nome da tabela no banco de dados
 */
@Entity
@Table(name = "Alunos")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"treinos", "medidas"})
public class Aluno {

    /**
     * Identificador único do aluno (chave primária)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // definindo id como chave primaria
    private Long id;
    
    /**
     * Nome completo do aluno
     */
    private String nome;
    
    /**
     * Email do aluno (único no sistema)
     */
    @Column(unique = true)
    private String email;
    
    /**
     * Senha de acesso do aluno (deve ser armazenada com hash em produção)
     */
    private String senha;

    /**
     * Instrutor responsável pelo aluno
     */
    @ManyToOne
    @JoinColumn(name = "instrutor_id") // relacionando aluno com instrutor
    private Instrutor instrutor;
    
    /**
     * Lista de treinos associados ao aluno
     */
    @OneToMany(mappedBy = "aluno")
    private List<Treino> treinos = new ArrayList<>();
    
    @OneToMany(mappedBy = "aluno")
    private List<Medida> medidas = new ArrayList<>();

    public Aluno() {
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
    
    public Instrutor getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }
    
    public List<Treino> getTreinos() {
        return treinos;
    }
    
    public void setTreinos(List<Treino> treinos) {
        this.treinos = treinos;
    }
    
    public List<Medida> getMedidas() {
        return medidas;
    }
    
    public void setMedidas(List<Medida> medidas) {
        this.medidas = medidas;
    }

}
