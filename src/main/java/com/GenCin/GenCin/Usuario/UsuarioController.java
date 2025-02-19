package com.GenCin.GenCin.Usuario;

import com.GenCin.GenCin.Sessao.SessaoService;
import com.GenCin.GenCin.Usuario.DTO.AlunoDTO;
import com.GenCin.GenCin.Usuario.DTO.ProfessorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SessaoService sessaoService;

    public UsuarioController(UsuarioService usuarioService, SessaoService sessaoService) {
        this.usuarioService = usuarioService;
        this.sessaoService = sessaoService;
    }

    // Função: /login
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String user, @RequestParam String senha) {
        try {
            Optional<UUID> idUser = usuarioService.verificarLogin(user, senha);
            if (idUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos.");
            }
            
            String keySessao = sessaoService.iniciarSessao(idUser.get(), user);
            return ResponseEntity.ok(keySessao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parâmetro inválido: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao realizar login: " + e.getMessage());
        }
    }


    @PostMapping("/cadastroprofessor")
    public ResponseEntity<String> cadastroProfessor(@RequestBody ProfessorDTO professorDTO) {
        try {
            if (usuarioService.verifyProfessor(professorDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Professor já existe.");
            }

            boolean sucesso = usuarioService.cadastrarProfessor(
                    professorDTO.getNome(),
                    professorDTO.getEmail(),
                    professorDTO.getSenha(),
                    professorDTO.getDepartamento()
            );

            return sucesso
                    ? ResponseEntity.status(HttpStatus.CREATED).body("Professor cadastrado com sucesso.")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar professor.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao realizar cadastro: " + e.getMessage());
        }
    }

    @PostMapping("/cadastroaluno")
    public ResponseEntity<String> cadastroAluno(@RequestBody AlunoDTO alunoDTO) {
        try {
            if (usuarioService.verifyAluno(alunoDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Aluno já existe.");
            }

            boolean sucesso = usuarioService.cadastrarAluno(
                    alunoDTO.getNome(),
                    alunoDTO.getEmail(),
                    alunoDTO.getSenha(),
                    alunoDTO.getMatricula()
            );

            return sucesso
                    ? ResponseEntity.status(HttpStatus.CREATED).body("Aluno cadastrado com sucesso.")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar aluno.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao realizar cadastro: " + e.getMessage());
        }
    }


    // Função: /getinfo
    @GetMapping("/getinfo")
    public ResponseEntity<?> getInfo(@RequestParam String keySessao) {
        try {
            // Verifique se a sessão é válida
            System.out.println("Chegou aqui");
            var idUser = sessaoService.verificarSessao(keySessao);

            if (idUser.isPresent()) {
                // Obtenha as informações do usuário pelo id
                Optional<List<String>> informacoesUsuario = usuarioService.obterInfo(idUser.get());

                return informacoesUsuario.isPresent()
                        ? ResponseEntity.ok(informacoesUsuario.get())
                        : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão inválida.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter informações: " + e.getMessage());
        }
    }



    // Função: /editardadosprofessor
    @PutMapping("/editardadosprofessor")
    public ResponseEntity<String> editarDadosProfessor(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String departamento) {
        try {
            boolean sucesso = usuarioService.editarDadosProfessor(nome, email, senha, departamento);
            return sucesso
                    ? ResponseEntity.ok("Dados do professor atualizados com sucesso.")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar dados do professor.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar dados: " + e.getMessage());
        }
    }

    // Função: /editardadosaluno
    @PutMapping("/editardadosaluno")
    public ResponseEntity<String> editarDadosAluno(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String matricula) {
        try {
            boolean sucesso = usuarioService.editarDadosAluno(nome, email, senha, matricula);
            return sucesso
                    ? ResponseEntity.ok("Dados do aluno atualizados com sucesso.")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar dados do aluno.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar dados: " + e.getMessage());
        }
    }

    // Função: /deletarconta
    @DeleteMapping("/deletarconta")
    public ResponseEntity<String> deletarConta(@RequestParam String keySessao) {
        try {
            var idUser = sessaoService.verificarSessao(keySessao);
            if (idUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão inválida.");
            }

            boolean sucesso = usuarioService.deletarUsuario(idUser.get());
            return sucesso
                    ? ResponseEntity.ok("Conta deletada com sucesso.")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao deletar conta.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar conta: " + e.getMessage());
        }
    }
}
