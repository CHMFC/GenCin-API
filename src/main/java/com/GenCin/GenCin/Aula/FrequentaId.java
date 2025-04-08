package com.GenCin.GenCin.Aula;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrequentaId implements Serializable {
    private UUID alunoId;
    private UUID aulaId;
}
