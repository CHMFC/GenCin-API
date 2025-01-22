package com.GenCin.GenCin.Usuario;

import com.GenCin.GenCin.Aluno.Aluno;
import com.GenCin.GenCin.Professor.Professor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Verifica login do usuário
    public Optional<UUID> verificarLogin(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isPresent()) {
            Usuario user = usuario.get();
            if ("ALUNO".equalsIgnoreCase(user.getTipo()) && user.getAluno() != null){
                if (senha.trim().equals(user.getAluno().getSenha().trim())) {
                    return Optional.of(user.getId());
                }
            } else if ("PROFESSOR".equalsIgnoreCase(user.getTipo()) && user.getProfessor() != null
                    && senha.trim().equals(user.getProfessor().getSenha().trim())) {
                return Optional.of(user.getId());
            }
        }

        return Optional.empty();
    }

    public Optional<List<String>> obterInfo(UUID idUsuario) {
        // Buscar o usuário pelo ID
        Optional<Usuario> usuarioOptional = usuarioRepository.findUsuarioById(idUsuario);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            List<String> informacoes = new ArrayList<>();

            // Verificar se é um aluno
            if ("ALUNO".equalsIgnoreCase(usuario.getTipo()) && usuario.getAluno() != null) {
                Aluno aluno = usuario.getAluno();
                informacoes.add(aluno.getNomeAluno());
                informacoes.add(aluno.getEmailAluno());
                informacoes.add("aluno");
                informacoes.add(aluno.getMatriculaAluno());
                return Optional.of(informacoes);
            }

            // Verificar se é um professor
            if ("PROFESSOR".equalsIgnoreCase(usuario.getTipo()) && usuario.getProfessor() != null) {
                Professor professor = usuario.getProfessor();
                informacoes.add(professor.getNome());
                informacoes.add(professor.getEmail());
                informacoes.add("professor");
                informacoes.add(professor.getDepartamento());
                return Optional.of(informacoes);
            }
        }

        return Optional.empty();
    }





    // Verifica se o professor já existe
    public boolean verifyProfessor(String email) {
        return usuarioRepository.existsProfessorByEmail(email);
    }

    // Verifica se o aluno já existe
    public boolean verifyAluno(String email) {
        return usuarioRepository.existsAlunoByEmail(email);
    }

    @Transactional
    public boolean cadastrarProfessor(String nome, String email, String senha, String departamento) {
        try {
            UUID idUsuario = UUID.randomUUID();
            usuarioRepository.inserirUsuario(idUsuario, "professor");
            usuarioRepository.cadastrarProfessorComUsuario(idUsuario, nome, email, senha, departamento);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean cadastrarAluno(String nome, String email, String senha, String matricula) {
        try {

            UUID idUsuario = UUID.randomUUID();
            usuarioRepository.inserirUsuario(idUsuario, "aluno");
            usuarioRepository.cadastrarAlunoComUsuario(idUsuario, nome, email, senha, matricula);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar dados do professor
    public boolean editarDadosProfessor(String nome, String email, String senha, String departamento) {
        return usuarioRepository.editarDadosProfessor(nome, email, senha, departamento) > 0;
    }

    // Editar dados do aluno
    public boolean editarDadosAluno(String nome, String email, String senha, String matricula) {
        return usuarioRepository.editarDadosAluno(nome, email, senha, matricula) > 0;
    }

    // Deletar usuário
    public boolean deletarUsuario(UUID id) {
        return usuarioRepository.deletarUsuario(id) > 0;
    }
}
