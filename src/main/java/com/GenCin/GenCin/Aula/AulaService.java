package com.GenCin.GenCin.Aula;

import com.GenCin.GenCin.Sessao.SessaoService;
import com.GenCin.GenCin.Usuario.Usuario;
import com.GenCin.GenCin.Usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;
    private final FrequentaRepository frequentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SessaoService sessaoService;

    public AulaService(AulaRepository aulaRepository, FrequentaRepository frequentaRepository,
                       UsuarioRepository usuarioRepository, SessaoService sessaoService) {
        this.aulaRepository = aulaRepository;
        this.frequentaRepository = frequentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.sessaoService = sessaoService;
    }

    // Criação de aula pelo professor
    @Transactional
    public Aula criarAula(String keySessao, Aula aula) throws Exception {
        Optional<UUID> idUserOpt = sessaoService.verificarSessao(keySessao);
        if (idUserOpt.isEmpty()) {
            throw new Exception("Sessão inválida.");
        }
        UUID idUser = idUserOpt.get();
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUser);
        if (usuarioOpt.isEmpty() || !"PROFESSOR".equalsIgnoreCase(usuarioOpt.get().getTipo())) {
            throw new Exception("Usuário não autorizado. Somente professores podem criar aulas.");
        }
        Usuario professor = usuarioOpt.get();
        aula.setProfessor(professor.getProfessor()); // assume que o objeto Usuario possui método getProfessor() que retorna o objeto Professor associado
        return aulaRepository.save(aula);
    }

    // Edição de aula pelo professor (somente se for o dono)
    @Transactional
    public Aula editarAula(String keySessao, UUID aulaId, Aula updatedAula) throws Exception {
        Optional<UUID> idUserOpt = sessaoService.verificarSessao(keySessao);
        if (idUserOpt.isEmpty()) {
            throw new Exception("Sessão inválida.");
        }
        UUID idUser = idUserOpt.get();
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUser);
        if (usuarioOpt.isEmpty() || !"PROFESSOR".equalsIgnoreCase(usuarioOpt.get().getTipo())) {
            throw new Exception("Usuário não autorizado. Somente professores podem editar aulas.");
        }
        Usuario professor = usuarioOpt.get();
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new Exception("Aula não encontrada."));
        // Verifica se o professor é o dono da aula
        if (!aula.getProfessor().getId().equals(professor.getProfessor().getId())) {
            throw new Exception("Você não pode editar uma aula que não é sua.");
        }
        // Atualiza os campos da aula
        aula.setCodAula(updatedAula.getCodAula());
        aula.setNomeAula(updatedAula.getNomeAula());
        aula.setDiasSemana(updatedAula.getDiasSemana());
        aula.setHoraInicio(updatedAula.getHoraInicio());
        aula.setHoraFim(updatedAula.getHoraFim());
        return aulaRepository.save(aula);
    }

    // Buscar aulas por cod_aula (retorna todas as aulas com o mesmo código)
    public List<Aula> buscarAulaPorCodigo(String codAula) {
        return aulaRepository.findByCodAula(codAula);
    }

    // Adicionar aula à agenda do aluno
    @Transactional
    public void adicionarAulaAgenda(String keySessao, UUID aulaId) throws Exception {
        Optional<UUID> idUserOpt = sessaoService.verificarSessao(keySessao);
        if (idUserOpt.isEmpty()) {
            throw new Exception("Sessão inválida.");
        }
        UUID idUser = idUserOpt.get();
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUser);
        if (usuarioOpt.isEmpty() || !"ALUNO".equalsIgnoreCase(usuarioOpt.get().getTipo())) {
            throw new Exception("Usuário não autorizado. Somente alunos podem adicionar aulas à agenda.");
        }
        Usuario aluno = usuarioOpt.get();
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new Exception("Aula não encontrada."));
        // Verifica se a aula já está na agenda
        if (frequentaRepository.findByAlunoAndAula(aluno, aula).isPresent()) {
            throw new Exception("Aula já adicionada à agenda.");
        }
        // Verifica conflito de horário com as aulas já adicionadas
        List<Frequenta> aulasAgenda = frequentaRepository.findByAluno(aluno);
        for (Frequenta f : aulasAgenda) {
            Aula existente = f.getAula();
            if (temConflitoDias(aula.getDiasSemana(), existente.getDiasSemana())) {
                if (horariosConflitam(aula.getHoraInicio(), aula.getHoraFim(), existente.getHoraInicio(), existente.getHoraFim())) {
                    throw new Exception("Conflito de horário com uma aula já adicionada à agenda.");
                }
            }
        }
        // Adiciona a aula à agenda
        Frequenta frequenta = Frequenta.builder()
                .id(new FrequentaId(aluno.getId(), aula.getId()))
                .aluno(aluno)
                .aula(aula)
                .build();
        frequentaRepository.save(frequenta);
    }

    // Remover aula da agenda do aluno
    @Transactional
    public void removerAulaAgenda(String keySessao, UUID aulaId) throws Exception {
        Optional<UUID> idUserOpt = sessaoService.verificarSessao(keySessao);
        if (idUserOpt.isEmpty()) {
            throw new Exception("Sessão inválida.");
        }
        UUID idUser = idUserOpt.get();
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUser);
        if (usuarioOpt.isEmpty() || !"ALUNO".equalsIgnoreCase(usuarioOpt.get().getTipo())) {
            throw new Exception("Usuário não autorizado. Somente alunos podem remover aulas da agenda.");
        }
        Usuario aluno = usuarioOpt.get();
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new Exception("Aula não encontrada."));
        Frequenta frequenta = frequentaRepository.findByAlunoAndAula(aluno, aula)
                .orElseThrow(() -> new Exception("Aula não está na agenda."));
        frequentaRepository.delete(frequenta);
    }

    // Helper: verifica se dois conjuntos de dias possuem interseção
    private boolean temConflitoDias(String dias1, String dias2) {
        String[] arrayDias1 = dias1.split(",");
        String[] arrayDias2 = dias2.split(",");
        for (String d1 : arrayDias1) {
            for (String d2 : arrayDias2) {
                if (d1.trim().equalsIgnoreCase(d2.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    // Helper: verifica se os horários conflitam
// Helper: verifica se os horários conflitam (usando java.sql.Time)
    private boolean horariosConflitam(Time inicio1, Time fim1, Time inicio2, Time fim2) {
        return inicio1.before(fim2) && fim1.after(inicio2);
    }

}
