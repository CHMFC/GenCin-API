package com.GenCin.GenCin.Atividade;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "atividade")
public class Atividade {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "cod_aula", nullable = false)
    private String codAula;

    @Column(name = "nome_atividade", nullable = false)
    private String nomeAtividade;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name = "nota_atividade")
    private Double notaAtividade;
}
