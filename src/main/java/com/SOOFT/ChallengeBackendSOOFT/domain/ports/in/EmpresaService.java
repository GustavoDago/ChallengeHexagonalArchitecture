package com.SOOFT.ChallengeBackendSOOFT.domain.ports.in;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmpresaService {
    Page<Empresa> empresasTransferenciasUltimoMes(Pageable pageable);
    Page<Empresa> empresasAdheridasUltimoMes(Pageable pageable);
    Empresa adherirEmpresa(Empresa empresaAAdherir);
}
