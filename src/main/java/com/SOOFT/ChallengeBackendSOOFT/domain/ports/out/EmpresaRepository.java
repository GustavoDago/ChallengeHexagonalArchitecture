package com.SOOFT.ChallengeBackendSOOFT.domain.ports.out;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface EmpresaRepository {
    Empresa save(Empresa empresa);
    Optional<Empresa> findByCuit(String cuit);
    List<Empresa> findByFechaAdhesion(LocalDate startDate, LocalDate endDate);
}
