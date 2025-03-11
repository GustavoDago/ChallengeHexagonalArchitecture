package com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.dto;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmpresaRequest(
        @NotBlank(message = "El CUIT no puede estar en blanco")
        @Pattern(regexp = "\\d{11}", message = "El CUIT debe tener 11 dígitos")
        String cuit,
        @Size(min = 3, max = 255, message = "La razón social debe tener entre 3 y 255 caracteres")
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
