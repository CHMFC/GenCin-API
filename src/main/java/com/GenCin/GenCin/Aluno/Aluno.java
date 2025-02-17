package com.GenCin.GenCin.Aluno;

import com.GenCin.GenCin.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nomeAluno;

    @Column(nullable = false, unique = true)
    private String emailAluno;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String matriculaAluno;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Usuario usuario;
}
