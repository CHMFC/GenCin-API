package com.GenCin.GenCin.Sessao;

import com.GenCin.GenCin.Security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.GenCin.GenCin.Gmail.SimpleEmailSender;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final TokenService tokenService;

    SimpleEmailSender simpleEmailSender;

    public SessaoService(SessaoRepository sessaoRepository, TokenService tokenService) {
        this.sessaoRepository = sessaoRepository;
        this.tokenService = tokenService;
    }

    /**
     * Verifica a sessão com base na chave da sessão.
     */
    public Optional<UUID> verificarSessao(String chaveSessao) {
        Optional<Boolean> expiredOptional = sessaoRepository.verificarSeSessaoExpirou(chaveSessao);
        if (expiredOptional.isEmpty() || expiredOptional.get()) {
            return Optional.empty();
        }
        Optional<LocalDateTime> horarioLoginOptional = sessaoRepository.buscarHorarioLoginPorChaveSessao(chaveSessao);
        if (horarioLoginOptional.isEmpty()) {
            return Optional.empty();
        }
        LocalDateTime horarioLogin = horarioLoginOptional.get();
        LocalDateTime agora = LocalDateTime.now();
        Duration diferenca = Duration.between(horarioLogin, agora);
        if (diferenca.toHours() <= 48) {
            return sessaoRepository.buscarIdUsuarioPorChaveSessao(chaveSessao);
        }
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
    @Transactional
    public String iniciarSessao(UUID idUsuario, String mail) {
        Optional<Sessao> sessaoExistente = sessaoRepository.buscarSessaoValidaPorUsuario(idUsuario);
        if (sessaoExistente.isPresent()) {
            // Expira a sessão existente
            expirarSessao(sessaoExistente.get().getChaveSessao());
        }

        // Gera uma nova chave de sessão (para identificar a sessão)
        String chaveSessao = UUID.randomUUID().toString();
        // Gera o token JWT usando o TokenService (com o id do usuário como subject)
        String chaveToken = tokenService.generateToken(idUsuario);
        UUID idSessao = UUID.randomUUID();

        // Gera um código de verificação a partir do token (opcional, apenas exemplo)
        // Cuidado: tokens JWT possuem pontos, então se preferir um código separado, gere-o de forma independente.

        String cod_verify = "";
        int loop = 0;
        while(loop < 1){
            cod_verify = chaveToken.substring(chaveToken.length() - 8, chaveToken.length() - 2).toUpperCase();
            System.out.println("cod: " + cod_verify);
            if (cod_verify.contains("_") || cod_verify.contains(".")) {
                System.out.println("Voltou");
            } else {
                loop++;
            }
        }

        System.out.println("Cod_verify: " + cod_verify);
        try {
            simpleEmailSender.sendEmailWithMessage(mail, cod_verify);
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }

        // Registra a nova sessão no banco de dados
        sessaoRepository.registrarSessao(idSessao, idUsuario, chaveSessao, chaveToken);
        return chaveToken;
    }

    public boolean validarBearerToken(String chaveSessao, String bearerToken) {
        Optional<Sessao> sessaoOpt = sessaoRepository.findByChaveSessao(chaveSessao);
        if (sessaoOpt.isPresent() && !sessaoOpt.get().getExpired()) {
            return sessaoOpt.get().getChave_token().equals(bearerToken);
        }
        return false;
    }

}