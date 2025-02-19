package com.GenCin.GenCin.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
@RepositoryRestResource(exported = false)
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // Verificar se um professor existe com base no email
    @Query("""
        SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END
        FROM Professor p
        WHERE p.email = :email
    """)
    boolean existsProfessorByEmail(@Param("email") String email);

    // Verificar se um aluno existe com base no email
    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
        FROM Aluno a
        WHERE a.emailAluno = :email
    """)
    boolean existsAlunoByEmail(@Param("email") String email);

    @Query("""
    SELECT u
    FROM Usuario u
    LEFT JOIN FETCH u.aluno
    LEFT JOIN FETCH u.professor
    WHERE u.id = :id
""")
    Optional<Usuario> findUsuarioById(@Param("id") UUID id);



    @Query("""
    SELECT u
    FROM Usuario u
    LEFT JOIN FETCH u.aluno
    LEFT JOIN FETCH u.professor
    WHERE u.aluno.emailAluno = :email OR u.professor.email = :email
""")
    Optional<Usuario> findByEmail(@Param("email") String email);


    // Editar dados de um professor
    @Transactional
    @Modifying
    @Query(value = """
        UPDATE professor
        SET nome = :nome, email = :email, senha = :senha, departamento = :departamento
        WHERE email = :email
    """, nativeQuery = true)
    int editarDadosProfessor(
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("senha") String senha,
            @Param("departamento") String departamento);

    // Editar dados de um aluno
    @Transactional
    @Modifying
    @Query(value = """
        UPDATE aluno
        SET nome_aluno = :nome, email_aluno = :email, senha = :senha, matricula_aluno = :matricula
        WHERE email_aluno = :email
    """, nativeQuery = true)
    int editarDadosAluno(
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("senha") String senha,
            @Param("matricula") String matricula);

    // Deletar um usuário (Professor ou Aluno)
    @Transactional
    @Modifying
    @Query(value = """
        DELETE FROM usuario
        WHERE id = :id
    """, nativeQuery = true)
    int deletarUsuario(@Param("id") UUID id);

    // Cadastrar um novo aluno
    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO aluno (id, nome_aluno, email_aluno, senha, matricula_aluno)
        VALUES (:id, :nome, :email, :senha, :matricula)
    """, nativeQuery = true)
    int cadastrarAlunoComUsuario(
            @Param("id") UUID id,
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("senha") String senha,
            @Param("matricula") String matricula);

    // Inserir novo usuário
    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO usuario (id, tipo, mailcheck)
        VALUES (:id, :tipo,  false)
    """, nativeQuery = true)
    void inserirUsuario(@Param("id") UUID id, @Param("tipo") String tipo);


    // Inserir professor com ID e retornar o número de linhas afetadas
    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO professor (id, nome, email, senha, departamento)
        VALUES (:id, :nome, :email, :senha, :departamento)
    """, nativeQuery = true)
    int cadastrarProfessorComUsuario(
            @Param("id") UUID id,
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("senha") String senha,
            @Param("departamento") String departamento);
}
