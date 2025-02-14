package com.GenCin.GenCin.Aula;

import com.GenCin.GenCin.Professor.Professor;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "aula")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aula {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "cod_aula")
    private String codAula;

    @Column(name = "nome_aula", nullable = false)
    private String nomeAula;

    @Column(name = "dia_aula", nullable = false)
    private String diaAula;

    @ManyToOne
    @JoinColumn(name = "id_professor", referencedColumnName = "id", nullable = false)
    private Professor professor;

    @Column(name = "hora_inicio", nullable = false)
    private Timestamp horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private Timestamp horaFim;
}
