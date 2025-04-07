package com.GenCin.GenCin.Aula;

import com.GenCin.GenCin.Aula.DTO.AulaDTO;
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

    public AulaService(AulaRepository aulaRepository,
                       FrequentaRepository frequentaRepository,
                       UsuarioRepository usuarioRepository,
                       SessaoService sessaoService) {
        this.aulaRepository = aulaRepository;
        this.frequentaRepository = frequentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.sessaoService = sessaoService;
    }

    // ==================
    //   CRIAR AULA
    // ==================
    @Transactional
    public AulaDTO criarAula(String keySessao, AulaDTO aulaDTO) throws Exception {
        Optional<UUID> idUserOpt = sessaoService.verificarSessao(keySessao);
        if (idUserOpt.isEmpty()) {
            throw new Exception("Sessão inválida.");
        }
        UUID idUser = idUserOpt.get();

        // Verifica se é professor
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUser);
        if (usuarioOpt.isEmpty() || !"PROFESSOR".equalsIgnoreCase(usuarioOpt.get().getTipo())) {
            throw new Exception("Usuário não autorizado. Somente professores podem criar aulas.");
        }

        // Converte o DTO para uma nova entidade "Aula"
        Aula aulaEntity = toEntity(aulaDTO, new Aula());
        // Vincula o professor
        aulaEntity.setProfessor(usuarioOpt.get().getProfessor());

        // Salva no banco
        Aula aulaSalva = aulaRepository.save(aulaEntity);

        // Converte de volta para DTO e retorna
        return toDTO(aulaSalva);
    }

    // ==================
    //   EDITAR AULA
    // ==================
    @Transactional
    public AulaDTO editarAula(String keySessao, UUID aulaId, AulaDTO updatedDTO) throws Exception {
        Optional<UUID> idUserOpt = sessaoService.verificarSessao(keySessao);
        if (idUserOpt.isEmpty()) {
            throw new Exception("Sessão inválida.");
        }
        UUID idUser = idUserOpt.get();

        // Verifica se é professor
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUser);
        if (usuarioOpt.isEmpty() || !"PROFESSOR".equalsIgnoreCase(usuarioOpt.get().getTipo())) {
            throw new Exception("Usuário não autorizado. Somente professores podem editar aulas.");
        }

        // Busca a aula existente
        Aula aulaExistente = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new Exception("Aula não encontrada."));

        // Verifica se o professor é o dono da aula
        if (!aulaExistente.getProfessor().getId().equals(usuarioOpt.get().getProfessor().getId())) {
            throw new Exception("Você não pode editar uma aula que não é sua.");
        }

        // Converte o DTO para a entidade, atualizando "aulaExistente"
        Aula aulaAtualizada = toEntity(updatedDTO, aulaExistente);
        // Professor não muda, então deixamos o que já está setado

        // Salva no banco
        Aula aulaSalva = aulaRepository.save(aulaAtualizada);

        // Retorna um DTO com as informações atualizadas
        return toDTO(aulaSalva);
    }

    // ============================
    //   BUSCAR AULAS POR CÓDIGO
    // ============================
    public List<AulaDTO> buscarAulaPorCodigoDTO(String codAula) {
        List<Aula> aulas = aulaRepository.findByCodAula(codAula);
        // Converte cada Aula para AulaDTO
        return aulas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // =====================
    //   ADICIONAR AGENDA
    // =====================
    @Transactional
    public void adicionarAulaAgenda(String keySessao, UUID aulaId) throws Exception {
        Optional<UUID> idUserOpt = sessaoService.verificarSessao(keySessao);
        if (idUserOpt.isEmpty()) {
            throw new Exception("Sessão inválida.");
        }
        UUID idUser = idUserOpt.get();

        // Verifica se é aluno
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUser);
        if (usuarioOpt.isEmpty() || !"ALUNO".equalsIgnoreCase(usuarioOpt.get().getTipo())) {
            throw new Exception("Usuário não autorizado. Somente alunos podem adicionar aulas à agenda.");
        }
        Usuario aluno = usuarioOpt.get();

        // Busca a aula
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new Exception("Aula não encontrada."));

        // Verifica se a aula já está na agenda
        if (frequentaRepository.findByAlunoAndAula(aluno, aula).isPresent()) {
            throw new Exception("Aula já adicionada à agenda.");
        }

        // Verifica conflitos de horário entre a aula a ser adicionada e as aulas já na agenda
        var aulasAgenda = frequentaRepository.findByAluno(aluno);
        for (var f : aulasAgenda) {
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

    // ======================
    //   REMOVER DA AGENDA
    // ======================
    @Transactional
    public void removerAulaAgenda(String keySessao, UUID aulaId) throws Exception {
        Optional<UUID> idUserOpt = sessaoService.verificarSessao(keySessao);
        if (idUserOpt.isEmpty()) {
            throw new Exception("Sessão inválida.");
        }
        UUID idUser = idUserOpt.get();

        // Verifica se é aluno
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUser);
        if (usuarioOpt.isEmpty() || !"ALUNO".equalsIgnoreCase(usuarioOpt.get().getTipo())) {
            throw new Exception("Usuário não autorizado. Somente alunos podem remover aulas da agenda.");
        }
        Usuario aluno = usuarioOpt.get();

        // Busca a aula
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new Exception("Aula não encontrada."));

        // Verifica se a aula está na agenda
        Frequenta frequenta = frequentaRepository.findByAlunoAndAula(aluno, aula)
                .orElseThrow(() -> new Exception("Aula não está na agenda."));
        frequentaRepository.delete(frequenta);
    }

    // ==============================
    //   GET MINHAS TURMAS (Agenda)
    // ==============================
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
            // Se for aluno, pega as aulas que ele frequenta
            aulas = frequentaRepository.findByAluno(usuario)
                    .stream()
                    .map(Frequenta::getAula)
                    .collect(Collectors.toList());
        } else if ("PROFESSOR".equalsIgnoreCase(usuario.getTipo())) {
            // Se for professor, pega as aulas criadas por ele
            aulas = aulaRepository.findByProfessor(usuario.getProfessor());
        } else {
            throw new Exception("Tipo de usuário não reconhecido.");
        }

        return aulas;
    }

    // ==============================
    //   MÉTODOS DE CONVERSÃO
    // ==============================

    // Converte uma entidade Aula -> AulaDTO
    private AulaDTO toDTO(Aula aula) {
        AulaDTO dto = new AulaDTO();
        dto.setId(aula.getId());
        dto.setCodAula(aula.getCodAula());
        dto.setNomeAula(aula.getNomeAula());

        // Seta apenas nome e email do professor
        if (aula.getProfessor() != null) {
            dto.setNomeProfessor(aula.getProfessor().getNome());
            dto.setEmailProfessor(aula.getProfessor().getEmail());
        }

        // Copiar os campos de dia e horário (caso queira expor):
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
    }

    // Converte um DTO -> Entidade Aula (para criar ou editar)
    // Se for edição, passamos a aula existente como `aula`, e atualizamos campos.
    private Aula toEntity(AulaDTO dto, Aula aula) {
        aula.setCodAula(dto.getCodAula());
        aula.setNomeAula(dto.getNomeAula());
        // Não definimos professor aqui, pois isso é feito dentro de criarAula/editarAula

        aula.setSeg(dto.isSeg());
        aula.setHoraInicioSeg(dto.getHoraInicioSeg());
        aula.setHoraFimSeg(dto.getHoraFimSeg());

        aula.setTer(dto.isTer());
        aula.setHoraInicioTer(dto.getHoraInicioTer());
        aula.setHoraFimTer(dto.getHoraFimTer());

        aula.setQua(dto.isQua());
        aula.setHoraInicioQua(dto.getHoraInicioQua());
        aula.setHoraFimQua(dto.getHoraFimQua());

        aula.setQui(dto.isQui());
        aula.setHoraInicioQui(dto.getHoraInicioQui());
        aula.setHoraFimQui(dto.getHoraFimQui());

        aula.setSex(dto.isSex());
        aula.setHoraInicioSex(dto.getHoraInicioSex());
        aula.setHoraFimSex(dto.getHoraFimSex());

        aula.setSab(dto.isSab());
        aula.setHoraInicioSab(dto.getHoraInicioSab());
        aula.setHoraFimSab(dto.getHoraFimSab());

        return aula;
    }

    // =======================
    //   CONFLITOS DE HORÁRIO
    // =======================
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

    private boolean horariosConflitam(Time inicio1, Time fim1, Time inicio2, Time fim2) {
        if (inicio1 == null || fim1 == null || inicio2 == null || fim2 == null) {
            return false;
        }
        return inicio1.before(fim2) && fim1.after(inicio2);
    }
}
