package com.GenCin.GenCin.Aula;

import com.GenCin.GenCin.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "frequenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Frequenta {

    @EmbeddedId
    private FrequentaId id;

    @ManyToOne
    @MapsId("alunoId")
    @JoinColumn(name = "id_aluno")
    private Usuario aluno;

    @ManyToOne
    @MapsId("aulaId")
    @JoinColumn(name = "id_aula")
    private Aula aula;
}
