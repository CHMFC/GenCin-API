package com.GenCin.GenCin.Aula;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.GenCin.GenCin.Professor.Professor;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
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

    @Column(name = "dias_semana", nullable = false)
    private String diasSemana;

    @ManyToOne
    @JoinColumn(name = "id_professor", referencedColumnName = "id", nullable = false)
    private Professor professor;

    // Usamos java.sql.Time e indicamos o formato "HH:mm:ss"
    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_inicio", nullable = false)
    private Time horaInicio;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_fim", nullable = false)
    private Time horaFim;
}
