package com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.controller;

import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.EmpresaYaExisteException;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.in.EmpresaService;
import com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.dto.EmpresaRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/empresas")
public class EmpresaController {
    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping("/transferencias-ultimo-mes")
    public ResponseEntity<Page<Empresa>> obtenerEmpresasConTransferenciasUltimoMes(Pageable pageable){
        Page<Empresa> list = empresaService.empresasTransferenciasUltimoMes(pageable);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/adheridas-ultimo-mes")
    public ResponseEntity<Page<Empresa>> obtenerEmpresasAdheridasUltimoMes(Pageable pageable){
        Page<Empresa> list = empresaService.empresasAdheridasUltimoMes(pageable);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> adherirEmpresa(@Valid @RequestBody EmpresaRequest empresaRequest){
        try {
            Empresa empresa = empresaRequest.toDomain();
            Empresa nuevaEmpresa = empresaService.adherirEmpresa(empresa);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEmpresa);
        } catch (EmpresaYaExisteException e) {
            throw e;
        }
    }
}
