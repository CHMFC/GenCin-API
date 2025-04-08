package com.GenCin.GenCin.Aula;

import com.GenCin.GenCin.Professor.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AulaRepository extends JpaRepository<Aula, UUID> {
    List<Aula> findByCodAula(String codAula);
    List<Aula> findByProfessor(Professor professor);
}

