package com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.repository;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Transferencia;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.TransferenciaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("mock")
public class MockTransferenciaRepositoryImpl implements TransferenciaRepository {

    private final List<Transferencia> transferencias = new ArrayList<>(); // Datos de ejemplo

    public MockTransferenciaRepositoryImpl() {
        // Inicializa con datos de ejemplo
        transferencias.add(new Transferencia(1L, new BigDecimal("100.00"), "123456789", "CuentaDebito1", "CuentaCredito1", LocalDate.now().minusDays(10)));
        transferencias.add(new Transferencia(2L, new BigDecimal("250.00"), "987654321", "CuentaDebito2", "CuentaCredito2", LocalDate.now().minusMonths(1)));
    }

    @Override
    public List<Transferencia> findByFechaBetweenAndIdEmpresa(LocalDate startDate, LocalDate endDate, String cuitEmpresa) {
        return transferencias.stream()
                .filter(transferencia -> transferencia.getFecha().isAfter(startDate.minusDays(1)) && transferencia.getFecha().isBefore(endDate.plusDays(1)))
                .filter(transferencia -> cuitEmpresa == null || transferencia.getIdEmpresa().equals(cuitEmpresa))
                .toList();
    }

    @Override
    public Transferencia save(Transferencia transferencia) {
        // Simula guardar la transferencia
        transferencia.setId((long) (transferencias.size() + 1)); // Simula el ID autogenerado
        transferencias.add(transferencia);
        return transferencia;
    }
}