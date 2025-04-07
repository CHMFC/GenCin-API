package com.GenCin.GenCin.Sessao;

import com.GenCin.GenCin.Aula.Aula;
import com.GenCin.GenCin.Aula.AulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessao")
public class SessaoController {

    private final SessaoService sessaoService;
    private final AulaService aulaService;

    public SessaoController(SessaoService sessaoService, AulaService aulaService) {
        this.sessaoService = sessaoService;
        this.aulaService = aulaService;
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

    @GetMapping("/getAulas")
    public ResponseEntity<?> getAulas(@RequestParam String keySessao) {
        System.out.println(keySessao);
        try {
            List<Aula> aulas = aulaService.getMinhasTurmas(keySessao);
            if (aulas == null || aulas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhuma aula encontrada para este usuário.");
            }
            return ResponseEntity.ok(aulas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parâmetros inválidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor: " + e.getMessage());
        }
    }
}
