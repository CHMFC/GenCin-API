package com.GenCin.GenCin.Sessao;

import com.GenCin.GenCin.Usuario.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessao", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sessao {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "chave_sessao", nullable = false, unique = true)
    private String chaveSessao;

    @Column(name = "horario_login", nullable = false)
    private LocalDateTime horarioLogin;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @Schema(description = "Associação com a tabela USUARIO. Uma sessão pertence a um usuário.")
    private Usuario usuario;

}
