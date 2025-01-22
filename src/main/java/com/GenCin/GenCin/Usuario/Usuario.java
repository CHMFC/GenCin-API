package com.GenCin.GenCin.Usuario;

import java.util.List;
import java.util.UUID;

import com.GenCin.GenCin.Aluno.Aluno;
import com.GenCin.GenCin.Professor.Professor;
import com.GenCin.GenCin.Sessao.Sessao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario", schema = "public")
@Schema(description = "Representa um usuário no sistema.")
public class Usuario {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ToString.Include
    @Column(nullable = false)
    @Schema(description = "Tipo do usuário (ex: ALUNO, PROFESSOR).", example = "ALUNO")
    private String tipo;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, optional = true)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @Schema(description = "Associação com a tabela ALUNO, se o usuário for um aluno.")
    private Aluno aluno;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, optional = true)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @Schema(description = "Associação com a tabela PROFESSOR, se o usuário for um professor.")
    private Professor professor;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "Associação com a tabela SESSAO. Um usuário pode ter várias sessões.")
    private List<Sessao> sessoes;

}
