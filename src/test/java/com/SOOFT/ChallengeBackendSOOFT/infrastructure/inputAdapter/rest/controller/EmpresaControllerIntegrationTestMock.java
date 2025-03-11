package com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.controller;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.in.EmpresaService;
import com.SOOFT.ChallengeBackendSOOFT.infrastructure.inputAdapter.rest.dto.EmpresaRequest;
import com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.repository.MockEmpresaRepositoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mock") // Activa el perfil "mock"
public class EmpresaControllerIntegrationTestMock {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    @Autowired
    private MockEmpresaRepositoryImpl mockEmpresaRepository;

    @Test
    void adherirEmpresa_conDatosValidos_retornaCreated() throws Exception {
        EmpresaRequest empresa = new EmpresaRequest("12345678901", "Empresa Test", LocalDate.now());

        mockMvc.perform(post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresa)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cuit").value("12345678901")); // Verifica el CUIT, por ejemplo
    }
    @Test
    void adherirEmpresa_conCuitDuplicado_retornaConflict() throws Exception {
        // Primero, adherimos una empresa
        EmpresaRequest empresa1 = new EmpresaRequest("11122233344", "Empresa Inicial", LocalDate.now());
        mockMvc.perform(post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresa1)))
                .andExpect(status().isCreated());

        // Luego, intentamos adherir OTRA empresa con el mismo CUIT
        EmpresaRequest empresa2 = new EmpresaRequest("11122233344", "Otra Empresa", LocalDate.now());
        mockMvc.perform(post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresa2)))
                .andExpect(status().isConflict());
    }


    @Test
    void obtenerEmpresasAdheridasUltimoMes_retornaListaCorrecta() throws Exception {

        mockMvc.perform(get("/api/empresas/adheridas-ultimo-mes")
                .param("page", "0") // Solicita la primera página
                .param("size", "10")) // Con un tamaño de página de 10
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())  //Verifica que la lista no esté vacía
                .andExpect(jsonPath("$.content[0].cuit").value("987654321")); //Verifica el CUIT

    }
}
