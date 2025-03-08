package com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.entity;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "Empresas")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaEntity {
    @Id
    private String CUIT; // uso al CUIT como clave primaria

    private String razonSocial;
    private LocalDate fechaAdhesion;


    // metodos de conversión con el Dominio
    public static EmpresaEntity fromDomain(Empresa empresa){
        return new EmpresaEntity(empresa.getCUIT(),
                empresa.getRazonSocial(),
                empresa.getFechaAdhesion());
    }

    public Empresa toDomain(){
        return new Empresa(this.CUIT,
                this.razonSocial,
                this.fechaAdhesion);
    }
}
