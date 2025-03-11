package com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.repository;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.EmpresaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("mock")
public class MockEmpresaRepositoryImpl implements EmpresaRepository {

    private final List<Empresa> empresas = new ArrayList<>(); // Datos de ejemplo

    public MockEmpresaRepositoryImpl() {
        // Inicializa con datos de ejemplo
        empresas.add(new Empresa("123456789", "Empresa Mock 1", LocalDate.now().minusDays(5)));
        empresas.add(new Empresa("987654321", "Empresa Mock 2", LocalDate.now().minusMonths(1)));
    }

    @Override
    public Empresa save(Empresa empresa) {
        empresas.add(empresa);
        return empresa; // Devuelve la empresa guardada (simulado)
    }

    @Override
    public Optional<Empresa> findByCuit(String cuit) {
        return empresas.stream()
                .filter(empresa -> empresa.getCUIT().equals(cuit))
                .findFirst();
    }

    @Override
    public Page<Empresa> findByFechaAdhesion(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<Empresa> empresasFiltradas = empresas.stream()
                .filter(empresa -> empresa.getFechaAdhesion().isAfter(startDate.minusDays(1)) && empresa.getFechaAdhesion().isBefore(endDate.plusDays(1)))
                .toList();
        //Simula paginación
        int pageSize = pageable.isPaged() ? pageable.getPageSize() : empresasFiltradas.size(); //Si no tiene paginación, devuelve el total
        int pageNumber = pageable.isPaged() ? pageable.getPageNumber() : 0;//Si no hay paginación, devuelve la primer página
        int start = (int) pageable.getOffset(); //Calcula el offset
        int end = Math.min((start + pageSize), empresasFiltradas.size());

        if(start > end) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        List<Empresa> empresasPaginadas = empresasFiltradas.subList(start, end);
        return new PageImpl<>(empresasPaginadas, pageable, empresasFiltradas.size());
    }
}
