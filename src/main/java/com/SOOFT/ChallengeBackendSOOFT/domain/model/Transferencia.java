package com.SOOFT.ChallengeBackendSOOFT.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transferencia {
    private Long id;
    private BigDecimal importe;
    private String idEmpresa;
    private String cuentaDebito;
    private String cuentaCredito;
    private LocalDate fecha;

    public Transferencia(Long id, BigDecimal importe, String idEmpresa, String cuentaDebito, String cuentaCredito, LocalDate fecha) {
        this.id = id;
        this.importe = importe;
        this.idEmpresa = idEmpresa;
        this.cuentaDebito = cuentaDebito;
        this.cuentaCredito = cuentaCredito;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCuentaDebito() {
        return cuentaDebito;
    }

    public void setCuentaDebito(String cuentaDebito) {
        this.cuentaDebito = cuentaDebito;
    }

    public String getCuentaCredito() {
        return cuentaCredito;
    }

    public void setCuentaCredito(String cuentaCredito) {
        this.cuentaCredito = cuentaCredito;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
