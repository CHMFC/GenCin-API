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

    @Column(name = "cod_aula", nullable = false)
    private String codAula;

    @Column(name = "nome_aula", nullable = false)
    private String nomeAula;

    @ManyToOne
    @JoinColumn(name = "id_professor", referencedColumnName = "id", nullable = false)
    private Professor professor;

    // Indica se a aula ocorre na segunda-feira
    @Column(name = "seg", nullable = false)
    private boolean seg;

    // Horário de início na segunda
    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_inicio_seg")
    private Time horaInicioSeg;

    // Horário de fim na segunda
    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_fim_seg")
    private Time horaFimSeg;

    // Indica se a aula ocorre na terça-feira
    @Column(name = "ter", nullable = false)
    private boolean ter;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_inicio_ter")
    private Time horaInicioTer;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_fim_ter")
    private Time horaFimTer;

    // Indica se a aula ocorre na quarta-feira
    @Column(name = "qua", nullable = false)
    private boolean qua;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_inicio_qua")
    private Time horaInicioQua;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_fim_qua")
    private Time horaFimQua;

    // Indica se a aula ocorre na quinta-feira
    @Column(name = "qui", nullable = false)
    private boolean qui;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_inicio_qui")
    private Time horaInicioQui;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_fim_qui")
    private Time horaFimQui;

    // Indica se a aula ocorre na sexta-feira
    @Column(name = "sex", nullable = false)
    private boolean sex;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_inicio_sex")
    private Time horaInicioSex;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_fim_sex")
    private Time horaFimSex;

    // Indica se a aula ocorre no sábado
    @Column(name = "sab", nullable = false)
    private boolean sab;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_inicio_sab")
    private Time horaInicioSab;

    @JsonFormat(pattern = "HH:mm:ss", timezone = "UTC")
    @Column(name = "hora_fim_sab")
    private Time horaFimSab;

}
