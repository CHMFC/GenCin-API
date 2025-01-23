package com.GenCin.GenCin.Sessao;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessaoService {

    private final SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    /**
     * Verifica a sessão com base na chave da sessão.
     */
    public Optional<UUID> verificarSessao(String chaveSessao) {
        // Verifica se a sessão está expirada
        Optional<Boolean> expiredOptional = sessaoRepository.verificarSeSessaoExpirou(chaveSessao);
        if (expiredOptional.isEmpty() || expiredOptional.get()) {
            return Optional.empty(); // Sessão não encontrada ou expirada
        }

        // Verifica o horário de login
        Optional<LocalDateTime> horarioLoginOptional = sessaoRepository.buscarHorarioLoginPorChaveSessao(chaveSessao);
        if (horarioLoginOptional.isEmpty()) {
            return Optional.empty(); // Sessão não encontrada
        }

        LocalDateTime horarioLogin = horarioLoginOptional.get();
        LocalDateTime agora = LocalDateTime.now();
        Duration diferenca = Duration.between(horarioLogin, agora);

        // Verifica se a sessão está dentro do limite de 24 horas
        if (diferenca.toHours() <= 24) {
            return sessaoRepository.buscarIdUsuarioPorChaveSessao(chaveSessao);
        }

        // Se fora do limite de 24 horas, expira a sessão
        expirarSessao(chaveSessao);
        return Optional.empty();
    }

    /**
     * Atualiza o status da sessão para expirada.
     */
    public void expirarSessao(String chaveSessao) {
        sessaoRepository.marcarSessaoComoExpirada(chaveSessao);
    }

    /**
     * Inicia uma nova sessão para um usuário.
     */
    public String iniciarSessao(UUID idUsuario) {
        // Gerar uma chave única para a sessão

        Optional<Sessao> sessaoExistente = sessaoRepository.buscarSessaoValidaPorUsuario(idUsuario);

        if (sessaoExistente.isPresent()) {
            // Retornar a chave da sessão existente
            expirarSessao(sessaoExistente.get().getChaveSessao());
        }

        String chaveSessao = UUID.randomUUID().toString();
        UUID idSessao = UUID.randomUUID(); // Gera um ID para a sessão

        // Salvar a nova sessão no banco de dados
        sessaoRepository.registrarSessao(idSessao, idUsuario, chaveSessao);

        return chaveSessao;
    }
}

