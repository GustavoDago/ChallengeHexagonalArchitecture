package com.SOOFT.ChallengeBackendSOOFT.domain.usecase;

import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.EmpresaYaExisteException;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.EmpresaRepository;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.TransferenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmpresaServiceImplTest {
    @Mock // Crea un mock del repositorio de empresas
    private EmpresaRepository empresaRepository;

    @Mock // Crea un mock del repositorio de transferencias
    private TransferenciaRepository transferenciaRepository;

    @InjectMocks // Inyecta los mocks en la implementación del caso de uso
    private EmpresaServiceImpl empresaService;

    @BeforeEach
        // Se ejecuta antes de cada test
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    void adherirEmpresa_CrearEmpresaExito() {
        // Arrange (Preparación)
        Empresa empresaAAdherir = new Empresa("12345678901", "Empresa Test", LocalDate.now());

        // Configura el comportamiento del mock del repositorio
        when(empresaRepository.findByCuit(empresaAAdherir.getCUIT())).thenReturn(Optional.empty()); // Simulamos que la empresa no existe
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresaAAdherir); // Simulamos que se guarda correctamente

        // Act (Ejecución)
        Empresa empresaCreada = empresaService.adherirEmpresa(empresaAAdherir);

        // Assert (Verificación)
        assertNotNull(empresaCreada);
        assertEquals(empresaAAdherir.getCUIT(), empresaCreada.getCUIT());
        assertEquals(empresaAAdherir.getRazonSocial(), empresaCreada.getRazonSocial());
        assertEquals(empresaAAdherir.getFechaAdhesion(), empresaCreada.getFechaAdhesion());

        // Verifica que se llamó a los métodos del repositorio
        verify(empresaRepository).findByCuit(empresaAAdherir.getCUIT());
        verify(empresaRepository).save(any(Empresa.class));
    }

    @Test
    void adherirEmpresa_ShouldThrowException_WhenCuitAlreadyExists() {
        // Arrange
        Empresa empresaExistente = new Empresa("12345678901", "Empresa Existente", LocalDate.now());
        Empresa empresaAAdherir = new Empresa("12345678901", "Otra Empresa", LocalDate.now());//Mismo CUIT

        // Configura el comportamiento del mock del repositorio para simular que la empresa ya existe
        when(empresaRepository.findByCuit(empresaAAdherir.getCUIT())).thenReturn(Optional.of(empresaExistente));

        // Act & Assert: Verifica que se lanza la excepción esperada
        assertThrows(EmpresaYaExisteException.class, () -> {
            empresaService.adherirEmpresa(empresaAAdherir);
        });

        // Verifica que se llamó al método findByCuit, pero NO a save
        verify(empresaRepository).findByCuit(empresaAAdherir.getCUIT());
        verify(empresaRepository, never()).save(any(Empresa.class)); //Verifica que no se guarda
    }
}