# Challenge Backend - SOOFT Technology
## Propósito del Challenge
El challenge se trata de generar los siguientes 3 endpoints:
- Uno que traiga las empresas que hicieron transferencias el último mes
- Otro que traiga las empresas que se adhirieron el último mes.
- El último que haga la adhesión de una empresa.

Requisitos: 
- Deseable: usar arquitectura hexagonal
- Base: puede usarse relacional o no relacional
- Datos de la empresa: CUIT, Razón Social, Fecha Adhesión
- Datos de la transferencia: Importe, Id Empresa, Cuenta Débito, Cuenta Crédito
- Las dudas en el desarrollo asumirlas y ponerlas en un archivo de aclaración
- Test Unitarios

## Decisiones de Diseño:
- Se desarrolló con arquitectura hexagonal.
- Debido a la simplicidad, se prefirió combinar la capa de aplicación con la de dominio, ya que agregar una capa,  
en este caso no es un beneficio significativo. Si en el futuro los casos de uso se vuelven más complejos, se reconsideraría  
la separación de la capa de aplicación.
- Se usa una base de datos H2, en memoria, por ahora que estamos en perfil desarrollador. En producción, se utilizará una  
base de datos persistente.
- Se asume que 'último mes' es el mes calendario anterior.
- Se asume que ```Transferencia -> IdEmpresa``` es ```Empresa -> CUIT```. 
- Se crean excepciones personalizadas.
- No se implementa seguridad.
## Cómo ejecutar la aplicación
### En IntelliJIdea
- Abrir el proyecto.
- Ir a carpeta src/main/java/com/SOOFT/ChallengeBackendSOOFT/
- Seleccionar archivo ChallengeBackendSooftApplication.java
- Seleccionar ```run```.
### En Terminal
- Abrir una terminal.
- Situarse en la carpeta raíz del proyecto.
- cd target.
- ejecutar ```java -jar ChallengeBackendSOOFT-0.0.1-SNAPSHOT.jar```.
### En Docker
- Ejecutar Docker Desktop, o Docker engine.
- Ir a la carpeta raíz del proyecto.
- Abrir Terminal.
- Ejecutar ```docker build -t sooft .``` para crear imagen del proyecto.
- Ejecutar ```docker run sooft``` para probar la aplicación.
## Estructura del proyecto
```
proyecto/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/SOOFT/ChallengeBackendSOOFT
│   │           ├── ChallengeBackendSooftApplication.java
│   │           ├── domain
│   │           │   ├── exceptions
│   │           │   │   ├── EmpresaYaExisteException.java
│   │           │   │   └── DatosInvalidosException.java
│   │           │   ├── model
│   │           │   │   ├── Empresa.java
│   │           │   │   └── Transferencia.java
│   │           │   ├── port
│   │           │   │   ├── in
│   │           │   │   │   └── EmpresaService.java
│   │           │   │   └── out
│   │           │   │       ├── EmpresaRepository.java
│   │           │   │       └── TransferenciaRepository.java
│   │           │   └── usecase
│   │           │       └── EmpresaServiceImpl.java
│   │           ├── infrastructure
│   │               ├── config
│   │               │   └── DatabaseConfig.java
│   │               │   └── OpenApiConfig.java
│   │               ├── inputadapter
│   │               │   └── rest
│   │               │       ├── controller
│   │               │       │   └── EmpresaController.java
│   │               │       └── dto
│   │               │           ├── EmpresaRequest.java
│   │               │           └── ErrorResponse.java
│   │               └── outputadapter
│   │                   ├── exception
│   │                   │    └── GlobalExceptionHandler.java
│   │                   ├── persistence
│   │                       ├── entity
│   │                       │   ├── EmpresaEntity.java
│   │                       │   └── TransferenciaEntity.java
│   │                       └── repository
│   │                           ├── EmpresaRepositoryImpl.java
│   │                           └── TransferenciaRepositoryImpl.java
│   └── test/
│       └── java/
│           └── com/SOOFT/ChallengeBackendSOOFT
│               ├── domain
│               │   └── usecase
│               │       └── EmpresaServiceImplTest.java
│               └── infrastructure
│                   └── inputadapter
│                        └── rest
│                             └── controller
│                                  └── EmpresaControllerIntegrationTest.java
└── pom.xml

```
## Endpoints de la API
Se utiliza Swagger, en http://localhost:8080/swagger-ui/index.html, para generar automáticamente la información de los endpoints.
## Posibles mejoras
- Paginación
- Autenticación
- Logging