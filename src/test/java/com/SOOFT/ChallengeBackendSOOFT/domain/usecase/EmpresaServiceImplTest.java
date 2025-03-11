package com.SOOFT.ChallengeBackendSOOFT.domain.usecase;

import com.SOOFT.ChallengeBackendSOOFT.application.service.EmpresaServiceImpl;
import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.DatosInvalidosException;
import com.SOOFT.ChallengeBackendSOOFT.domain.exceptions.EmpresaYaExisteException;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.model.Transferencia;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.EmpresaRepository;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.TransferenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
    void adherirEmpresa_ShouldThrowEmpresaYaExisteException_WhenCuitAlreadyExists() {
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

    @Test
    void adherirEmpresa_ShouldThrowDatosInvalidosException_WhenRazonSocialIsNullOrEmpty() {
        // Arrange
        Empresa empresaAAdherir = new Empresa("1111", null, LocalDate.now()); // Razon social nula
        Empresa empresaAAdherir2 = new Empresa("1111", "", LocalDate.now()); // Razon social vacía


        // Act & Assert: Verifica que se lanza la excepción esperada
        assertThrows(DatosInvalidosException.class, () -> {
            empresaService.adherirEmpresa(empresaAAdherir);
        });
        assertThrows(DatosInvalidosException.class, () -> {
            empresaService.adherirEmpresa(empresaAAdherir2);
        });

        // Verifica que NO se llamó a ningún método del repositorio
        verify(empresaRepository, never()).findByCuit(anyString());
        verify(empresaRepository, never()).save(any(Empresa.class));
    }

    @Test
    void empresasAdheridasUltimoMes_ShouldReturnListOfEmpresas() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());

        Empresa empresa1 = new Empresa("1111", "Empresa 1", firstDayOfLastMonth);
        Empresa empresa2 = new Empresa("2222", "Empresa 2", lastDayOfLastMonth);
        List<Empresa> empresasAdheridas = List.of(empresa1, empresa2);

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Empresa> empresasPage = new PageImpl<>(empresasAdheridas, pageable, empresasAdheridas.size());

        when(empresaRepository.findByFechaAdhesion(firstDayOfLastMonth, lastDayOfLastMonth,pageable))
                .thenReturn(empresasPage);

        // Act
        Page<Empresa> result = empresaService.empresasAdheridasUltimoMes(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(empresa1, result.getContent().get(0));
        assertEquals(empresa2, result.getContent().get(1));

        verify(empresaRepository).findByFechaAdhesion(firstDayOfLastMonth, lastDayOfLastMonth,pageable);
    }

    @Test
    void empresasTransferenciasUltimoMes_ShouldReturnListOfEmpresas_whenTransferenciasExist() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());

        Empresa empresa1 = new Empresa("1111", "Empresa 1", LocalDate.now().minusMonths(2)); //Adherida hace 2 meses
        Empresa empresa2 = new Empresa("2222", "Empresa 2", LocalDate.now().minusMonths(3)); //Adherida hace 3 meses
        Transferencia transferencia1 = new Transferencia(1L,  new BigDecimal(100), "1111", "C1", "C2", firstDayOfLastMonth);
        Transferencia transferencia2 = new Transferencia(2L, new BigDecimal(200), "2222", "C3", "C4", lastDayOfLastMonth);
        Transferencia transferencia3 = new Transferencia(3L, new BigDecimal(300), "1111", "C5", "C6", firstDayOfLastMonth); //Otra transf de empresa 1
        List<Transferencia> transferenciasUltimoMes = List.of(transferencia1, transferencia2, transferencia3);

        //Usa PageImpl y Pageable
        Pageable pageable = PageRequest.of(0, 10);;
        List<Empresa> empresas = List.of(empresa1, empresa2);
        Page<Empresa> empresasPage = new PageImpl<>(empresas);

        when(transferenciaRepository.findByFechaBetweenAndIdEmpresa(firstDayOfLastMonth, lastDayOfLastMonth, null))
                .thenReturn(transferenciasUltimoMes);
        when(empresaRepository.findByCuit("1111")).thenReturn(Optional.of(empresa1));
        when(empresaRepository.findByCuit("2222")).thenReturn(Optional.of(empresa2));

        // Act
        Page<Empresa> result = empresaService.empresasTransferenciasUltimoMes(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements()); // Devuelve las dos empresas
        assertTrue(result.getContent().contains(empresa1));
        assertTrue(result.getContent().contains(empresa2));
        verify(transferenciaRepository).findByFechaBetweenAndIdEmpresa(firstDayOfLastMonth, lastDayOfLastMonth, null);
        verify(empresaRepository).findByCuit("1111");
        verify(empresaRepository).findByCuit("2222");

    }

    @Test
    void empresasTransferenciasUltimoMes_ShouldReturnEmptyList_whenNoTransferencias() {
        //Arrange
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());

        // Usa Page y Pageable
        Pageable pageable = PageRequest.of(0, 10);;
        Page<Empresa> emptyPage = Page.empty();

        when(transferenciaRepository.findByFechaBetweenAndIdEmpresa(firstDayOfLastMonth, lastDayOfLastMonth, null))
                .thenReturn(List.of()); // Lista vacía

        //Act
        Page<Empresa> result = empresaService.empresasTransferenciasUltimoMes(pageable);

        //Assert
        assertTrue(result.isEmpty());
        verify(transferenciaRepository).findByFechaBetweenAndIdEmpresa(firstDayOfLastMonth, lastDayOfLastMonth, null);
        verify(empresaRepository, never()).findByCuit(anyString()); // No se llama a findByCuit
    }

    @Test
    void empresasTransferenciasUltimoMes_ShouldReturnEmptyList_whenEmpresaNotFound() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());

        Transferencia transferencia1 = new Transferencia(1L, new BigDecimal(100), "1111", "C1", "C2", firstDayOfLastMonth);

        List<Transferencia> transferenciasUltimoMes = List.of(transferencia1);

        // Usa Page y Pageable
        Pageable pageable = PageRequest.of(0, 10);;
        Page<Empresa> emptyPage = Page.empty();

        when(transferenciaRepository.findByFechaBetweenAndIdEmpresa(firstDayOfLastMonth, lastDayOfLastMonth, null))
                .thenReturn(transferenciasUltimoMes);
        when(empresaRepository.findByCuit("1111")).thenReturn(Optional.empty()); // Simulamos que no existe

        // Act
        Page<Empresa> result = empresaService.empresasTransferenciasUltimoMes(pageable);

        // Assert
        assertTrue(result.isEmpty()); // La lista debe estar vacía
        verify(transferenciaRepository).findByFechaBetweenAndIdEmpresa(firstDayOfLastMonth, lastDayOfLastMonth, null);
        verify(empresaRepository).findByCuit("1111"); //Se llamó, pero no la encontró

    }
}