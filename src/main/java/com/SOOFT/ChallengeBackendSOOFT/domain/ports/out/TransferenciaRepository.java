package com.SOOFT.ChallengeBackendSOOFT.domain.ports.out;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Transferencia;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransferenciaRepository {
    List<Transferencia> findByFechaBetweenAndIdEmpresa(LocalDate startDate, LocalDate endDate, String cuitEmpresa);
    Transferencia save(Transferencia transferencia);
}
