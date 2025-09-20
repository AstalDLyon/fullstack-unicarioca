package com.Unigym.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Unigym.entities.Medida;

public interface MedidaRepository extends JpaRepository<Medida, Long> {
    
    // Encontrar todas as medidas de um aluno
    List<Medida> findByAlunoId(Long alunoId);
    
    // Encontrar medidas de um aluno ordenadas por data (mais recentes primeiro)
    List<Medida> findByAlunoIdOrderByDataDesc(Long alunoId);
    
    // Encontrar medidas em um período específico
    List<Medida> findByAlunoIdAndDataBetweenOrderByDataDesc(Long alunoId, LocalDate dataInicio, LocalDate dataFim);
    
    // Encontrar a medida mais recente de um aluno
    Medida findFirstByAlunoIdOrderByDataDesc(Long alunoId);
    
    // Consulta personalizada para obter um resumo das medidas (apenas peso e % gordura) para gráficos
    @Query("SELECT NEW map(m.data as data, m.peso as peso, m.percentualGordura as percentualGordura) " +
            "FROM Medida m WHERE m.aluno.id = ?1 ORDER BY m.data")
    List<Object> findMedidasResumidasByAlunoId(Long alunoId);
}