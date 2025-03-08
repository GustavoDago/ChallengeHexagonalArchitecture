package com.SOOFT.ChallengeBackendSOOFT.domain.usecase;

import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.DatosInvalidosException;
import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.EmpresaYaExisteException;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Transferencia;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.in.EmpresaService;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.EmpresaRepository;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.TransferenciaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<Empresa> empresasTransferenciasUltimoMes() {
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

        // obtengo las empresas a partir de la lista de CUITs
        return cuitsEmpresas.stream()
                .map(empresaRepository::findByCuit)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public List<Empresa> empresasAdheridasUltimoMes() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = today.minusMonths(1)
                .withDayOfMonth(today.minusMonths(1).lengthOfMonth());

        return empresaRepository.findByFechaAdhesion(firstDayOfLastMonth,lastDayOfLastMonth);
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
