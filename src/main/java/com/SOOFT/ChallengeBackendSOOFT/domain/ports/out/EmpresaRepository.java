package com.SOOFT.ChallengeBackendSOOFT.domain.ports.out;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface EmpresaRepository {
    Empresa save(Empresa empresa);
    Optional<Empresa> findByCuit(String cuit);
    Page<Empresa> findByFechaAdhesion(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
