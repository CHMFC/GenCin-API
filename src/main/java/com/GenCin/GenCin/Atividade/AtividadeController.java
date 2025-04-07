package com.GenCin.GenCin.Atividade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/atividade")
public class AtividadeController {

    private final AtividadeService atividadeService;

    public AtividadeController(AtividadeService atividadeService) {
        this.atividadeService = atividadeService;
    }

    // Criar nova atividade
    @PostMapping("/criar")
    public ResponseEntity<?> criarAtividade(@RequestBody Atividade atividade) {
        try {
            Atividade nova = atividadeService.criarAtividade(atividade);
            return ResponseEntity.status(HttpStatus.CREATED).body(nova);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar atividade: " + e.getMessage());
        }
    }

    // Buscar todas as atividades
    @GetMapping
    public ResponseEntity<List<Atividade>> buscarTodas() {
        List<Atividade> atividades = atividadeService.buscarTodas();
        return ResponseEntity.ok(atividades);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        Optional<Atividade> atividadeOpt = atividadeService.buscarPorId(id);

        if (atividadeOpt.isPresent()) {
            // Se encontrou a atividade, retorna ResponseEntity<Atividade>
            return ResponseEntity.ok(atividadeOpt.get());
        } else {
            // Caso contrário, retorna ResponseEntity<String> (mensagem)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada.");
        }
    }


    // Buscar por codAula (caso queira filtrar pela coluna cod_aula)
    @GetMapping("/cod")
    public ResponseEntity<?> buscarPorCod(@RequestParam String codAula) {
        List<Atividade> lista = atividadeService.buscarPorCodAula(codAula);
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma atividade encontrada para esse codAula.");
        }
        return ResponseEntity.ok(lista);
    }

    // Editar atividade
    @PutMapping("/{id}")
    public ResponseEntity<?> editarAtividade(@PathVariable UUID id, @RequestBody Atividade nova) {
        try {
            Atividade atualizada = atividadeService.editarAtividade(id, nova);
            return ResponseEntity.ok(atualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao editar atividade: " + e.getMessage());
        }
    }

    // Deletar atividade
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAtividade(@PathVariable UUID id) {
        boolean deletada = atividadeService.deletarAtividade(id);
        if (deletada) {
            return ResponseEntity.ok("Atividade deletada com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada.");
        }
    }
}
