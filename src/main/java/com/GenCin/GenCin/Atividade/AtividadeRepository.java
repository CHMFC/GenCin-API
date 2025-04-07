package com.GenCin.GenCin.Atividade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AtividadeRepository extends JpaRepository<Atividade, UUID> {

    // Exemplo de método para buscar por codAula
    List<Atividade> findByCodAula(String codAula);
}
