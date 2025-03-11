# Challenge Backend - SOOFT Technology
## Versión 0.0.2
## Propósito del Challenge
Este proyecto implementa una API RESTful para gestionar empresas y transferencias, cumpliendo con los siguientes requisitos:

*   Obtener las empresas que realizaron transferencias en el último mes calendario.
*   Obtener las empresas que se adhirieron en el último mes calendario.
*   Adherir una nueva empresa.

## Requisitos: 
*   **Arquitectura:**  Hexagonal (Puertos y Adaptadores).
*   **Base de Datos:**  Relacional (H2 en memoria para desarrollo, configurable para producción).
*   **Datos de la Empresa:** CUIT, Razón Social, Fecha de Adhesión.
*   **Datos de la Transferencia:** Importe, Id Empresa (CUIT), Cuenta Débito, Cuenta Crédito.
*   **Testing:** Unitario y de Integración.

## Decisiones de Diseño y aclaraciones:
*   **Arquitectura Hexagonal:** Se implementa una arquitectura hexagonal para desacoplar las diferentes capas de la aplicación (dominio, aplicación, infraestructura), promoviendo la mantenibilidad, escalabilidad y testabilidad.
*   **Separación de Capas:** El dominio, la aplicación y la infraestructura están claramente separados.
    *   **Dominio:** Contiene las entidades (`Empresa`, `Transferencia`), las excepciones de dominio (`EmpresaYaExisteException`), y las interfaces de los repositorios.  Es independiente de cualquier framework.
    *   **Aplicación:**  Contiene los *casos de uso* (servicios de aplicación) que orquestan la lógica de negocio, utilizando los repositorios (a través de sus interfaces) y las entidades del dominio.
    *   **Infraestructura:** Implementa los adaptadores para interactuar con el mundo exterior (base de datos, controladores REST, etc.).  Depende de frameworks como Spring Boot, Spring Data JPA, Spring Security, etc.
*   **Base de Datos:** Se utiliza H2 en memoria para el perfil de desarrollo (`dev`) para facilitar la ejecución y las pruebas.  Para producción, se puede configurar fácilmente una base de datos relacional persistente (PostgreSQL, MySQL, etc.) modificando el archivo `application.properties` (o `application.yml`) y las dependencias en `pom.xml`.
*   **"Último Mes":** Se interpreta como el mes calendario anterior completo.
*   **`Transferencia.IdEmpresa`:** Se asume que corresponde al `Empresa.CUIT`.
*   **Excepciones Personalizadas:** Se utilizan excepciones de dominio (`EmpresaYaExisteException`) y de aplicación (`DatosInvalidosException`, que ahora se considera una excepcion de infraestructura) para un manejo de errores más preciso.
*   **Seguridad:**
    *   **Autenticación:** Se implementa autenticación básica (Basic Auth) usando Spring Security.  Se requiere el rol "ADMIN" para acceder a los endpoints.
    *   **Rate Limiting:** Se implementa rate limiting usando Bucket4j para prevenir ataques de fuerza bruta y denegación de servicio. Se limita a 10 peticiones por minuto.
    * **Protección contra Inyección SQL:** Se utiliza Spring Data JPA/Hibernate con parámetros precompilados, lo que protege contra la inyección SQL.
    *   **Protección contra XSS:** Se utilizan las protecciones XSS integradas de Spring Security.
*   **Validaciones:** Se realizan validaciones exhaustivas en los DTOs de entrada (`EmpresaRequest`) usando anotaciones de Jakarta Validation (`@NotBlank`, `@Pattern`, `@Size`, `@NotNull`).
*   **Paginación:** Se implementa paginación en los endpoints que devuelven listas de empresas, utilizando `Pageable` y `Page` de Spring Data.
* **Logging:** Se utiliza SLF4J con Logback para el logging (aunque no se muestra explícitamente en el código proporcionado, se asume que está configurado).
* **Perfiles:** Se utilizan perfiles de Spring (`dev` y `mock`) para configurar diferentes entornos de ejecución (base de datos en memoria, datos mockeados, etc.).
## Cómo ejecutar la aplicación
### Requisitos Previos

*   Java 23 (o superior, compatible con Spring Boot 3.4.3)
*   Maven
*   (Opcional) Docker y Docker Compose

### Ejecución con IntelliJ IDEA

1.  Abre el proyecto en IntelliJ IDEA.
2.  Localiza la clase `ChallengeBackendSooftApplication`.
3.  Haz clic derecho en la clase y selecciona "Run 'ChallengeBackendSooftApplication'".

### Ejecución con Maven (línea de comandos)

1.  Abre una terminal en la raíz del proyecto.
2.  Ejecuta: `mvn spring-boot:run`

### Ejecución con Docker

1.  Asegúrate de tener Docker instalado y en ejecución.
2.  Abre una terminal en la raíz del proyecto.
3.  Construye la imagen Docker: `docker build -t sooft .`
4.  Ejecuta el contenedor: `docker run -p 8080:8080 sooft` (esto mapea el puerto 8080 del contenedor al puerto 8080 de tu máquina).
## Estructura del proyecto
```
proyecto/
├── src/
│ ├── main/
│ │ └── java/
│ │ └── com/SOOFT/ChallengeBackendSOOFT
│ │ ├── ChallengeBackendSooftApplication.java
│ │ ├── application/
│ │ │ └── usecase/
│ │ │ ├── AdherirEmpresaUseCase.java
│ │ │ ├── ObtenerEmpresasAdheridasUltimoMesUseCase.java
│ │ │ └── ObtenerEmpresasConTransferenciasUltimoMesUseCase.java
│ │ ├── domain/
│ │ │ ├── exceptions/
│ │ │ │ └── EmpresaYaExisteException.java
│ │ │ ├── model/
│ │ │ │ ├── Empresa.java
│ │ │ │ └── Transferencia.java
│ │ │ └── ports/
│ │ │ ├── in/ (Vacío - los casos de uso son los "puertos de entrada")
│ │ │ └── out/
│ │ │ ├── EmpresaRepository.java
│ │ │ └── TransferenciaRepository.java
│ │ ├── infrastructure/
│ │ ├── config/
│ │ │ ├── DevConfig.java
│ │ │ ├── MockConfig.java
│ │ │ ├── OpenApiConfig.java
│ │ │ ├── RateLimitConfig.java
│ │ │ ├── SecurityConfig.java
│ │ │ └── TestMockConfig.java
│ │ ├── inputadapter/
│ │ │ └── rest/
│ │ │ ├── controller/
│ │ │ │ └── EmpresaController.java
│ │ │ └── dto/
│ │ │ └── EmpresaRequest.java
│ │ └── outputadapter/
│ │ ├── exception/
│ │ │ └── GlobalExceptionHandler.java
│ │ ├── persistence/
│ │ │ ├── entity/
│ │ │ │ ├── EmpresaEntity.java
│ │ │ │ └── TransferenciaEntity.java
│ │ │ └── repository/
│ │ │ ├── DevEmpresaRepositoryImpl.java
│ │ │ ├── DevTransferenciaRepositoryImpl.java
│ │ │ ├── MockEmpresaRepositoryImpl.java
│ │ │ └── MockTransferenciaRepositoryImpl.java
│ └── test/
│ └── java/
│ └── com/SOOFT/ChallengeBackendSOOFT/
│ ├── application/
│ │ └── usecase/
│ │ └── AdherirEmpresaUseCaseTest.java
│ └── infrastructure/
│ └── inputadapter/
│ └── rest/
│ └── controller/
│ ├── EmpresaControllerIntegrationTest.java
│ └── EmpresaControllerIntegrationTestMock.java
├── pom.xml
├── README.md
└── application.yml
```
## Endpoints de la API
Se utiliza Swagger, en http://localhost:8080/swagger-ui/index.html, para generar automáticamente la información de los endpoints.

## Tests

El proyecto incluye tests unitarios (para los casos de uso) y tests de integración (para los controladores, usando MockMvc y probando la integración con la base de datos y Spring Security).

*   **Tests Unitarios:**  Se encuentran en `src/test/java/.../application/usecase`.  Usan Mockito para simular las dependencias (repositorios).
*   **Tests de Integración:** Se encuentran en `src/test/java/.../infrastructure/inputadapter/rest/controller`.  Usan `@SpringBootTest`, `@AutoConfigureMockMvc`, `@ActiveProfiles`, y `@Transactional` para probar la integración completa de la aplicación, incluyendo la base de datos, Spring Security y el rate limiting. Se utiliza el perfil "dev" para las pruebas de integración con la base de datos, y "mock" para pruebas con datos simulados.
    *   **Autenticación en Tests:** Los tests de integración se autentican usando `with(httpBasic("admin", "admin"))` de `SecurityMockMvcRequestPostProcessors`.
    *   **Rate Limiting en Tests:** Se incluyen tests específicos para verificar el correcto funcionamiento del rate limiting.

Para ejecutar los tests:

*   **IntelliJ IDEA:** Haz clic derecho en la carpeta `src/test/java` y selecciona "Run Tests".
*   **Maven:**  `mvn test`