package com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.entity;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Transferencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal importe;
    private String idEmpresa; //   es el CUIT de Empresa
    private String cuentaDebito;
    private String cuentaCredito;
    private LocalDate fecha;

    // métodos de conversión con el Dominio
    public static TransferenciaEntity fromDomain(Transferencia transferencia){
        return new TransferenciaEntity(transferencia.getId(),
                transferencia.getImporte(),
                transferencia.getIdEmpresa(),
                transferencia.getCuentaDebito(),
                transferencia.getCuentaCredito(),
                transferencia.getFecha());
    }

    public Transferencia toDomain(){
        return new Transferencia(this.id,
                this.importe,
                this.idEmpresa,
                this.cuentaDebito,
                this.cuentaCredito,
                this.fecha);
    }
}
