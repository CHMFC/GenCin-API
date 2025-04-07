package com.GenCin.GenCin.Sessao;

import com.GenCin.GenCin.Aula.Aula;
import com.GenCin.GenCin.Aula.AulaService;
import com.GenCin.GenCin.Aula.DTO.AulaDTO;
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

            // Mapeia cada Aula para AulaDTO
            List<AulaDTO> aulaDTOs = aulas.stream().map(aula -> {
                AulaDTO dto = new AulaDTO();
                dto.setId(aula.getId());
                dto.setCodAula(aula.getCodAula());
                dto.setNomeAula(aula.getNomeAula());

                // Pega só nome e email do professor
                if (aula.getProfessor() != null) {
                    dto.setNomeProfessor(aula.getProfessor().getNome());
                    dto.setEmailProfessor(aula.getProfessor().getEmail());
                }

                // Mapeia os campos de dia/horário
                dto.setSeg(aula.isSeg());
                dto.setHoraInicioSeg(aula.getHoraInicioSeg());
                dto.setHoraFimSeg(aula.getHoraFimSeg());

                dto.setTer(aula.isTer());
                dto.setHoraInicioTer(aula.getHoraInicioTer());
                dto.setHoraFimTer(aula.getHoraFimTer());

                dto.setQua(aula.isQua());
                dto.setHoraInicioQua(aula.getHoraInicioQua());
                dto.setHoraFimQua(aula.getHoraFimQua());

                dto.setQui(aula.isQui());
                dto.setHoraInicioQui(aula.getHoraInicioQui());
                dto.setHoraFimQui(aula.getHoraFimQui());

                dto.setSex(aula.isSex());
                dto.setHoraInicioSex(aula.getHoraInicioSex());
                dto.setHoraFimSex(aula.getHoraFimSex());

                dto.setSab(aula.isSab());
                dto.setHoraInicioSab(aula.getHoraInicioSab());
                dto.setHoraFimSab(aula.getHoraFimSab());

                return dto;
            }).toList();

            // Retorna a lista de DTOs
            return ResponseEntity.ok(aulaDTOs);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Parâmetros inválidos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }

}
