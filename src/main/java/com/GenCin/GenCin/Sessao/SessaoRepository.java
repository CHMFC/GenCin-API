package com.GenCin.GenCin.Sessao;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface SessaoRepository extends JpaRepository<Sessao, UUID> {

    // Registrar uma nova sessão
    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO sessao (id, id_usuario, chave_sessao, horario_login, expired)
        VALUES (:id, :idUsuario, :chaveSessao, CURRENT_TIMESTAMP, false)
    """, nativeQuery = true)
    void registrarSessao(@Param("id") UUID id, @Param("idUsuario") UUID idUsuario, @Param("chaveSessao") String chaveSessao);

    // Verificar se a sessão está expirada
    @Query("""
        SELECT s.expired
        FROM Sessao s
        WHERE s.chaveSessao = :chaveSessao
    """)
    Optional<Boolean> verificarSeSessaoExpirou(@Param("chaveSessao") String chaveSessao);

    // Buscar horário de login da sessão
    @Query("""
        SELECT s.horarioLogin
        FROM Sessao s
        WHERE s.chaveSessao = :chaveSessao
    """)
    Optional<LocalDateTime> buscarHorarioLoginPorChaveSessao(@Param("chaveSessao") String chaveSessao);

    // Buscar ID do usuário associado à sessão
    @Query("""
        SELECT s.usuario.id
        FROM Sessao s
        WHERE s.chaveSessao = :chaveSessao
    """)
    Optional<UUID> buscarIdUsuarioPorChaveSessao(@Param("chaveSessao") String chaveSessao);

    // Marcar a sessão como expirada
    @Transactional
    @Modifying
    @Query("""
        UPDATE Sessao s
        SET s.expired = true
        WHERE s.chaveSessao = :chaveSessao
    """)
    void marcarSessaoComoExpirada(@Param("chaveSessao") String chaveSessao);

    @Query("""
    SELECT s
    FROM Sessao s
    WHERE s.usuario.id = :idUsuario AND s.expired = false
""")
    Optional<Sessao> buscarSessaoValidaPorUsuario(@Param("idUsuario") UUID idUsuario);
}
