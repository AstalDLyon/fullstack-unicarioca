package com.Unigym.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Medidas")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Medida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate data;
    private Double peso;
    private Double altura;
    private Double imc;
    private Double percentualGordura;
    private Double circunferenciaBraco;
    private Double circunferenciaPeitoral;
    private Double circunferenciaCintura;
    private Double circunferenciaQuadril;
    private Double circunferenciaCoxa;
    private Double circunferenciaPanturrilha;
    private String observacoes;
    
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;
    
    @ManyToOne
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;
    
    public Medida() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
        calcularIMC();
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
        calcularIMC();
    }
    
    // Método para calcular o IMC
    private void calcularIMC() {
        if (peso != null && altura != null && altura > 0) {
            this.imc = peso / (altura * altura);
        }
    }

    public Double getImc() {
        return imc;
    }

    public Double getPercentualGordura() {
        return percentualGordura;
    }

    public void setPercentualGordura(Double percentualGordura) {
        this.percentualGordura = percentualGordura;
    }

    public Double getCircunferenciaBraco() {
        return circunferenciaBraco;
    }

    public void setCircunferenciaBraco(Double circunferenciaBraco) {
        this.circunferenciaBraco = circunferenciaBraco;
    }

    public Double getCircunferenciaPeitoral() {
        return circunferenciaPeitoral;
    }

    public void setCircunferenciaPeitoral(Double circunferenciaPeitoral) {
        this.circunferenciaPeitoral = circunferenciaPeitoral;
    }

    public Double getCircunferenciaCintura() {
        return circunferenciaCintura;
    }

    public void setCircunferenciaCintura(Double circunferenciaCintura) {
        this.circunferenciaCintura = circunferenciaCintura;
    }

    public Double getCircunferenciaQuadril() {
        return circunferenciaQuadril;
    }

    public void setCircunferenciaQuadril(Double circunferenciaQuadril) {
        this.circunferenciaQuadril = circunferenciaQuadril;
    }

    public Double getCircunferenciaCoxa() {
        return circunferenciaCoxa;
    }

    public void setCircunferenciaCoxa(Double circunferenciaCoxa) {
        this.circunferenciaCoxa = circunferenciaCoxa;
    }

    public Double getCircunferenciaPanturrilha() {
        return circunferenciaPanturrilha;
    }

    public void setCircunferenciaPanturrilha(Double circunferenciaPanturrilha) {
        this.circunferenciaPanturrilha = circunferenciaPanturrilha;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }
}