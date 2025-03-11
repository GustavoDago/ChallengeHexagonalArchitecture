package com.SOOFT.ChallengeBackendSOOFT.application.service;

import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.DatosInvalidosException;
import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.EmpresaYaExisteException;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Transferencia;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.in.EmpresaService;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.EmpresaRepository;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.TransferenciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final TransferenciaRepository transferenciaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, TransferenciaRepository transferenciaRepository) {
        this.empresaRepository = empresaRepository;
        this.transferenciaRepository = transferenciaRepository;
    }

    @Override
    public Page<Empresa> empresasTransferenciasUltimoMes(Pageable pageable) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = today.minusMonths(1)
                .withDayOfMonth(today.minusMonths(1).lengthOfMonth());

        // obtener las transferencias del último mes
        List<Transferencia> transferencias = transferenciaRepository.findByFechaBetweenAndIdEmpresa(
                firstDayOfLastMonth,
                lastDayOfLastMonth,
                null
        );

        // recupero los CUITs de las empresas que hicieron transferencias
        List<String> cuitsEmpresas = transferencias.stream()
                .map(Transferencia::getIdEmpresa)
                .distinct()
                .toList();
        //En lugar de crear un PageImpl a partir de una lista filtrada manualmente,
        // ahora se itera sobre los cuits y se obtienen las empresas paginadas directamente del repositorio.
        List<Empresa> empresas = cuitsEmpresas.stream()
                .map(empresaRepository::findByCuit)
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), empresas.size());

        if (start > end) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        return new PageImpl<>(empresas.subList(start,end), pageable, empresas.size());
    }

    @Override
    public Page<Empresa> empresasAdheridasUltimoMes(Pageable pageable) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = today.minusMonths(1)
                .withDayOfMonth(today.minusMonths(1).lengthOfMonth());

        return empresaRepository.findByFechaAdhesion(firstDayOfLastMonth,lastDayOfLastMonth, pageable);
    }

    @Override
    public Empresa adherirEmpresa(Empresa empresaAAdherir) {
        // validaciones
        if (empresaAAdherir.getCUIT() == null || empresaAAdherir.getCUIT().isBlank()){
            throw new DatosInvalidosException("El CUIT no debe ser nulo o vacío");
        }
        if (empresaAAdherir.getRazonSocial() == null || empresaAAdherir.getRazonSocial().isBlank()){
            throw new DatosInvalidosException("La Razón Social no debe ser nula o vacía");
        }
        if (empresaRepository.findByCuit(empresaAAdherir.getCUIT()).isPresent()){
            throw new EmpresaYaExisteException("Ya existe una empresa con el CUIT: "+
                    empresaAAdherir.getCUIT());
        }

        //persistir
        return empresaRepository.save(empresaAAdherir);
    }
}
