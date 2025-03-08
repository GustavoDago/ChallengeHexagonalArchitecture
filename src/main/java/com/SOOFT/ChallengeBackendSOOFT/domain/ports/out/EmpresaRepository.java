package com.SOOFT.ChallengeBackendSOOFT.domain.ports.out;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository {
    Empresa save(Empresa empresa);
    Optional<Empresa> findByCuit(String cuit);
    List<Empresa> findByFechaAdhesion(LocalDate startDate, LocalDate endDate);
}
