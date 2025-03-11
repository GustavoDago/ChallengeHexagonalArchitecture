package com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.controller;

import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.EmpresaYaExisteException;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.in.EmpresaService;
import com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.dto.EmpresaRequest;
import io.github.bucket4j.Bucket;
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
    private final Bucket bucket;
    public EmpresaController(EmpresaService empresaService, Bucket bucket) {
        this.empresaService = empresaService;
        this.bucket = bucket;
    }

    @GetMapping("/transferencias-ultimo-mes")
    public ResponseEntity<Page<Empresa>> obtenerEmpresasConTransferenciasUltimoMes(Pageable pageable){
        if (bucket.tryConsume(1)) {
            Page<Empresa> list = empresaService.empresasTransferenciasUltimoMes(pageable);
            return ResponseEntity.ok(list);
        }
        // Si no hay tokens disponibles, retorna 429 Too Many Requests
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping("/adheridas-ultimo-mes")
    public ResponseEntity<Page<Empresa>> obtenerEmpresasAdheridasUltimoMes(Pageable pageable){
        if (bucket.tryConsume(1)) {
            Page<Empresa> list = empresaService.empresasAdheridasUltimoMes(pageable);
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping
    public ResponseEntity<?> adherirEmpresa(@Valid @RequestBody EmpresaRequest empresaRequest){
        if (bucket.tryConsume(1)) {
            try {
                Empresa empresa = empresaRequest.toDomain();
                Empresa nuevaEmpresa = empresaService.adherirEmpresa(empresa);
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEmpresa);
            } catch (EmpresaYaExisteException e) {
                throw e;
            }
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
