package com.GenCin.GenCin.Aula;

import com.GenCin.GenCin.Usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FrequentaRepository extends JpaRepository<Frequenta, FrequentaId> {
    List<Frequenta> findByAluno(Usuario aluno);
    Optional<Frequenta> findByAlunoAndAula(Usuario aluno, Aula aula);
}
