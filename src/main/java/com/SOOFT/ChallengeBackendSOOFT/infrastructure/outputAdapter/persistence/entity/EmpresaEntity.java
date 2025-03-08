package com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Table(name = "Empresas")
@Data
@Entity
public class EmpresaEntity {
    @Id
    private String CUIT;
    private String razonSocial;
    private LocalDate fechaAdhesion;
}
