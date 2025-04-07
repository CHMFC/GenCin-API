package com.GenCin.GenCin.Atividade;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    public AtividadeService(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    // Criar nova atividade
    public Atividade criarAtividade(Atividade atividade) {
        return atividadeRepository.save(atividade);
    }

    // Buscar todas as atividades
    public List<Atividade> buscarTodas() {
        return atividadeRepository.findAll();
    }

    // Buscar atividades por codAula (caso queira filtrar por código)
    public List<Atividade> buscarPorCodAula(String codAula) {
        return atividadeRepository.findByCodAula(codAula);
    }

    // Buscar por ID
    public Optional<Atividade> buscarPorId(UUID id) {
        return atividadeRepository.findById(id);
    }

    // Editar atividade (ex.: PUT)
    public Atividade editarAtividade(UUID id, Atividade nova) throws Exception {
        Atividade existente = atividadeRepository.findById(id)
                .orElseThrow(() -> new Exception("Atividade não encontrada."));

        existente.setCodAula(nova.getCodAula());
        existente.setNomeAtividade(nova.getNomeAtividade());
        existente.setDescricao(nova.getDescricao());
        existente.setDataEntrega(nova.getDataEntrega());
        existente.setNotaAtividade(nova.getNotaAtividade());

        return atividadeRepository.save(existente);
    }

    // Deletar atividade
    public boolean deletarAtividade(UUID id) {
        if (atividadeRepository.existsById(id)) {
            atividadeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
