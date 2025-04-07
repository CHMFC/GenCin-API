package com.GenCin.GenCin.Aula;

import com.GenCin.GenCin.Sessao.SessaoService;
import com.GenCin.GenCin.Usuario.Usuario;
import com.GenCin.GenCin.Usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        // Define o professor da aula a partir do objeto associado ao usuário
        aula.setProfessor(professor.getProfessor());
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

        // Atualiza os dias e horários
        aula.setSeg(updatedAula.isSeg());
        aula.setHoraInicioSeg(updatedAula.getHoraInicioSeg());
        aula.setHoraFimSeg(updatedAula.getHoraFimSeg());

        aula.setTer(updatedAula.isTer());
        aula.setHoraInicioTer(updatedAula.getHoraInicioTer());
        aula.setHoraFimTer(updatedAula.getHoraFimTer());

        aula.setQua(updatedAula.isQua());
        aula.setHoraInicioQua(updatedAula.getHoraInicioQua());
        aula.setHoraFimQua(updatedAula.getHoraFimQua());

        aula.setQui(updatedAula.isQui());
        aula.setHoraInicioQui(updatedAula.getHoraInicioQui());
        aula.setHoraFimQui(updatedAula.getHoraFimQui());

        aula.setSex(updatedAula.isSex());
        aula.setHoraInicioSex(updatedAula.getHoraInicioSex());
        aula.setHoraFimSex(updatedAula.getHoraFimSex());

        aula.setSab(updatedAula.isSab());
        aula.setHoraInicioSab(updatedAula.getHoraInicioSab());
        aula.setHoraFimSab(updatedAula.getHoraFimSab());

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
        // Verifica conflitos de horário entre a aula a ser adicionada e as aulas já na agenda
        List<Frequenta> aulasAgenda = frequentaRepository.findByAluno(aluno);
        for (Frequenta f : aulasAgenda) {
            Aula existente = f.getAula();
            if (existeConflito(aula, existente)) {
                throw new Exception("Conflito de horário com uma aula já adicionada à agenda.");
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

    // Helper: Verifica se há conflito entre os horários de duas aulas para os dias em comum
    private boolean existeConflito(Aula a1, Aula a2) {
        // Segunda-feira
        if (a1.isSeg() && a2.isSeg() && horariosConflitam(a1.getHoraInicioSeg(), a1.getHoraFimSeg(), a2.getHoraInicioSeg(), a2.getHoraFimSeg())) {
            return true;
        }
        // Terça-feira
        if (a1.isTer() && a2.isTer() && horariosConflitam(a1.getHoraInicioTer(), a1.getHoraFimTer(), a2.getHoraInicioTer(), a2.getHoraFimTer())) {
            return true;
        }
        // Quarta-feira
        if (a1.isQua() && a2.isQua() && horariosConflitam(a1.getHoraInicioQua(), a1.getHoraFimQua(), a2.getHoraInicioQua(), a2.getHoraFimQua())) {
            return true;
        }
        // Quinta-feira
        if (a1.isQui() && a2.isQui() && horariosConflitam(a1.getHoraInicioQui(), a1.getHoraFimQui(), a2.getHoraInicioQui(), a2.getHoraFimQui())) {
            return true;
        }
        // Sexta-feira
        if (a1.isSex() && a2.isSex() && horariosConflitam(a1.getHoraInicioSex(), a1.getHoraFimSex(), a2.getHoraInicioSex(), a2.getHoraFimSex())) {
            return true;
        }
        // Sábado
        if (a1.isSab() && a2.isSab() && horariosConflitam(a1.getHoraInicioSab(), a1.getHoraFimSab(), a2.getHoraInicioSab(), a2.getHoraFimSab())) {
            return true;
        }
        return false;
    }

    // Helper: Verifica se dois intervalos de horário conflitam (usando java.sql.Time)
    private boolean horariosConflitam(Time inicio1, Time fim1, Time inicio2, Time fim2) {
        if (inicio1 == null || fim1 == null || inicio2 == null || fim2 == null) {
            return false;
        }
        return inicio1.before(fim2) && fim1.after(inicio2);
    }

    @Transactional(readOnly = true)
    public List<Aula> getMinhasTurmas(String keySessao) throws Exception {
        Optional<UUID> idUserOpt = sessaoService.verificarSessao(keySessao);
        if (idUserOpt.isEmpty()) {
            throw new Exception("Sessão inválida.");
        }
        UUID idUser = idUserOpt.get();

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUser);
        if (usuarioOpt.isEmpty()) {
            throw new Exception("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        List<Aula> aulas;

        if ("ALUNO".equalsIgnoreCase(usuario.getTipo())) {
            // Se for aluno, pega as aulas que ele frequenta (usando o FrequentaRepository)
            // Supondo que o método findByAluno retorne uma lista de Frequenta e que cada Frequenta possua a referência para a Aula:
            aulas = frequentaRepository.findByAluno(usuario)
                    .stream()
                    .map(Frequenta::getAula)
                    .collect(Collectors.toList());
        } else if ("PROFESSOR".equalsIgnoreCase(usuario.getTipo())) {
            // Se for professor, pega as aulas cujo professor é ele.
            // Supondo que o Usuario possua um método getProfessor() que retorna o objeto Professor
            aulas = aulaRepository.findByProfessor(usuario.getProfessor());
        } else {
            throw new Exception("Tipo de usuário não reconhecido.");
        }

        return aulas;
    }

}
