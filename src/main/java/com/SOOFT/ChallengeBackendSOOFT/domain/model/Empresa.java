package com.SOOFT.ChallengeBackendSOOFT.domain.model;

import java.time.LocalDate;

public class Empresa {
    private String CUIT;
    private String razonSocial;
    private LocalDate fechaAdhesion;

    public String getCUIT() {
        return CUIT;
    }

    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public LocalDate getFechaAdhesion() {
        return fechaAdhesion;
    }

    public void setFechaAdhesion(LocalDate fechaAdhesion) {
        this.fechaAdhesion = fechaAdhesion;
    }
}
