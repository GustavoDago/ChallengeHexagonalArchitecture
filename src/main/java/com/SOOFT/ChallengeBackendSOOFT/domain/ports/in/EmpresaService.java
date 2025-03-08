package com.SOOFT.ChallengeBackendSOOFT.domain.ports.in;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;

import java.util.List;

public interface EmpresaService {
    List<Empresa> empresasTransferenciasUltimoMes();
    List<Empresa> empresasAdheridasUltimoMes();
    Empresa adherirEmpresa(Empresa empresaAAdherir);
}
