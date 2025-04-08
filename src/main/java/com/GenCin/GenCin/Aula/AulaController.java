package com.GenCin.GenCin.Aula;

import com.GenCin.GenCin.Aula.DTO.AulaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/aula")
public class AulaController {

    private final AulaService aulaService;

    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getAulaInfo(@RequestParam UUID aulaId) {
        try {
            // Chama o service para buscar a aula por ID
            AulaDTO aulaDTO = aulaService.buscarAulaPorIdDTO(aulaId);
            return ResponseEntity.ok(aulaDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarAula(@RequestParam String keySessao, @RequestBody AulaDTO aulaDTO) {
        try {
            // Agora, o service retorna AulaDTO
            AulaDTO novaAula = aulaService.criarAula(keySessao, aulaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaAula);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para professor editar aula (somente se for o dono)
    @PutMapping("/editar")
    public ResponseEntity<?> editarAula(@RequestParam String keySessao,
                                        @RequestParam UUID aulaId,
                                        @RequestBody AulaDTO aulaDTO) {
        try {
            AulaDTO aulaAtualizada = aulaService.editarAula(keySessao, aulaId, aulaDTO);
            return ResponseEntity.ok(aulaAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para buscar aulas pelo código (retornando lista de DTOs)
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarAula(@RequestParam String codAula) {
        try {
            List<AulaDTO> aulas = aulaService.buscarAulaPorCodigoDTO(codAula);
            return ResponseEntity.ok(aulas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para aluno adicionar aula à agenda (sem mexer no DTO, pois não retornamos a aula)
    @PostMapping("/adicionarAgenda")
    public ResponseEntity<?> adicionarAulaAgenda(@RequestParam String keySessao, @RequestParam UUID aulaId) {
        try {
            aulaService.adicionarAulaAgenda(keySessao, aulaId);
            return ResponseEntity.ok("Aula adicionada à agenda com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para aluno remover aula da agenda
    @DeleteMapping("/removerAgenda")
    public ResponseEntity<?> removerAulaAgenda(@RequestParam String keySessao, @RequestParam UUID aulaId) {
        try {
            aulaService.removerAulaAgenda(keySessao, aulaId);
            return ResponseEntity.ok("Aula removida da agenda com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
