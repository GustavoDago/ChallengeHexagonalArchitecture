package com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.dto;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmpresaRequest(
        @NotBlank(message = "El CUIT no puede estar en blanco")
        String cuit,
        @NotBlank(message = "La razón social no puede estar en blanco")
        String razonSocial,

        @NotNull(message = "La fecha de adhesión no puede ser nula")
        LocalDate fechaAdhesion
)
{
    public Empresa toDomain() {
        return new Empresa(this.cuit, this.razonSocial, this.fechaAdhesion);
    }
}
