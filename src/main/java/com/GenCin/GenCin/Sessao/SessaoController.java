package com.GenCin.GenCin.Sessao;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessao")
public class SessaoController {

    private final SessaoService sessaoService;

    public SessaoController(SessaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    /**
     * API REST para logout de uma sessão.
     * Marca a sessão como expirada com base na chave fornecida.
     *
     * @param chaveSessao Chave única da sessão.
     * @return Código 200 se o logout foi bem-sucedido, ou 400 se não conseguiu fazer logout.
     */
    @GetMapping("/logout")
    public ResponseEntity<String> fazerLogout(@RequestParam String chaveSessao) {
        try {
            sessaoService.expirarSessao(chaveSessao);
            return ResponseEntity.ok("Logout realizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Não foi possível realizar o logout: " + e.getMessage());
        }
    }
}
