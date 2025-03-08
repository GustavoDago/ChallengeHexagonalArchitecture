package com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.controller;

import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.EmpresaYaExisteException;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.in.EmpresaService;
import com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.dto.EmpresaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // prueba de integración
@AutoConfigureMockMvc
@Transactional // ejecuta transacciones, y hace rollback al final
class EmpresaControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc; // simular peticiones HTTP

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ObjectMapper objectMapper; // convertimos objetos a JSON

    @Test
    void adherirEmpresa_ShouldCreateEmpresaSuccessfully() throws Exception {
        // Arrange
        EmpresaRequest empresaRequest = new EmpresaRequest("12345678901", "Empresa Test", LocalDate.now());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresaRequest))) // Convierte el DTO a JSON
                .andExpect(status().isCreated()) //  201 Created
                .andExpect(jsonPath("$.cuit").value(empresaRequest.cuit()))
                .andExpect(jsonPath("$.razonSocial").value(empresaRequest.razonSocial()))
                .andExpect(jsonPath("$.fechaAdhesion").value(empresaRequest.fechaAdhesion().toString()));
    }

    @Test
    void adherirEmpresa_ShouldReturnBadRequest_WhenCuitAlreadyExists() throws Exception {
        // Arrange
        Empresa empresaExistente = new Empresa("11111111111", "Empresa Existente", LocalDate.now());
        empresaService.adherirEmpresa(empresaExistente); //La creamos primero

        EmpresaRequest empresaRequest = new EmpresaRequest("11111111111", "Otra Empresa", LocalDate.now());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresaRequest)))
                .andExpect(status().isConflict());
    }
    @Test
    void adherirEmpresa_ShouldReturnBadRequest_WhenCuitIsInvalid() throws Exception{
        //Arrange
        EmpresaRequest empresaRequest = new EmpresaRequest("", "Otra Empresa", LocalDate.now()); //Cuit vacío
        EmpresaRequest empresaRequest2 = new EmpresaRequest(null, "Otra Empresa", LocalDate.now()); //Cuit null

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresaRequest)))
                .andExpect(status().isBadRequest()); //  400 Bad Request, porque el CUIT es invalido

        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresaRequest2)))
                .andExpect(status().isBadRequest()); //  400 Bad Request, porque el CUIT es invalido
    }

    @Test
    void adherirEmpresa_ShouldReturnBadRequest_WhenRazonSocialIsInvalid() throws Exception{
        //Arrange
        EmpresaRequest empresaRequest = new EmpresaRequest("123", "", LocalDate.now()); //Razon social vacía
        EmpresaRequest empresaRequest2 = new EmpresaRequest("123", null, LocalDate.now()); //Razon social null

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresaRequest)))
                .andExpect(status().isBadRequest()); //  400 Bad Request, porque la razon social es invalida

        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresaRequest2)))
                .andExpect(status().isBadRequest()); //  400 Bad Request, porque la razon social es invalida
    }
}