package com.Unigym.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Unigym.entities.Aluno;

/**
 * AlunoRepository
 * 
 * Interface de repositório que gerencia o acesso a dados da entidade Aluno.
 * Estende JpaRepository para herdar operações CRUD básicas e operações de paginação.
 * 
 * Esta interface fornece métodos para:
 * - Operações CRUD padrão (herdadas de JpaRepository)
 * - Consultas específicas para alunos, como busca por email
 * 
 * O Spring Data JPA implementa automaticamente esta interface em tempo de execução,
 * criando as consultas SQL necessárias com base nos nomes dos métodos definidos.
 * 
 * Parâmetros genéricos:
 * - Aluno: A entidade gerenciada pelo repositório
 * - Long: O tipo do identificador (ID) da entidade
 */
public interface AlunoRepository extends JpaRepository<Aluno, Long>{
    /**
     * Busca um aluno pelo seu endereço de email
     * 
     * Este método é usado principalmente para autenticação e verificação
     * de unicidade de email durante o cadastro de novos alunos.
     * 
     * @param email O endereço de email do aluno a ser buscado
     * @return O aluno encontrado ou null se não existir
     */
    Aluno findByEmail(String email);
}
