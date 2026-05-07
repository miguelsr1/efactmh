# Analisis Tecnico del Proyecto `efactmh`

Fecha del analisis: 2026-03-21  
Ruta analizada: `C:\Users\msanchez\Documents\PERSONAL\JAVA\efactmh`

## 1) Resumen ejecutivo

El proyecto es una aplicacion Java EE (WAR) con JSF/PrimeFaces, JDBI y consumo HTTP hacia servicios externos de facturacion.  
La base es funcional, pero hay deuda tecnica importante en 4 ejes:

- mantenibilidad (clases muy grandes y logica mezclada en capas de vista),
- seguridad/configuracion (endpoints hardcodeados y manejo parcial de cookies/sesiones),
- consistencia de build/dependencias (plugins Maven y versiones mezcladas),
- calidad (sin pruebas automatizadas).

## 2) Inventario rapido

- Java source files: `127`
- Recursos web (`src/main/webapp`): `263`
- Recursos (`src/main/resources`): `5`
- Tests en `src/test`: `0` archivos

Distribucion por modulo Java (`sv/com/jsoft/efactmh`):

- `model`: 52
- `view`: 21
- `services`: 18
- `util`: 8
- `security`: 3
- otros: 25

## 3) Hallazgos priorizados

### Alta prioridad

1. Configuracion sensible y endpoints fijos en codigo/propiedades
- Evidencia:
  - `src/main/java/sv/com/jsoft/efactmh/util/RestUtil.java:44` (`HOST = "http://localhost:8082"`)
  - `src/main/resources/variables.properties:1`
  - `src/main/resources/variables.properties:5`
- Riesgo: despliegues fragiles por ambiente, cambios manuales y riesgo de exponer infraestructura.
- Recomendacion: externalizar a variables de entorno/JNDI MicroProfile Config y separar perfiles `dev/test/prod`.

2. Inconsistencia de DataSource en seguridad vs acceso a datos
- Evidencia:
  - `src/main/java/sv/com/jsoft/efactmh/security/ApplicationConfig.java:12` (`java:/FacturaDS`)
  - `src/main/java/sv/com/jsoft/efactmh/db/DataSourceApp.java:14` (`java:/EfactDS`)
- Riesgo: autenticacion y datos de negocio pueden apuntar a fuentes distintas accidentalmente.
- Recomendacion: unificar nombre JNDI por ambiente y documentar contrato de datasource.

3. Sin pruebas automatizadas
- Evidencia: `src/test` existe pero con `0` archivos.
- Riesgo: regresiones altas en facturacion, impuestos, y flujo de emision DTE.
- Recomendacion: agregar pruebas unitarias de calculos (`InvoceView`, `DteService`) e integracion de repositorios/servicios.

### Media prioridad

4. Clase de vista con exceso de responsabilidades
- Evidencia:
  - `src/main/java/sv/com/jsoft/efactmh/view/InvoceView.java:53` (clase principal, 675 lineas)
  - `src/main/java/sv/com/jsoft/efactmh/view/InvoceView.java:516` (persistencia + envio DTE + UI)
- Riesgo: alta complejidad ciclomatica, dificil de probar y de evolucionar.
- Recomendacion: extraer casos de uso a servicio de aplicacion (`InvoiceWorkflowService`) y dejar la vista como orquestador UI.

5. Manejo de errores con retornos `null` y excepciones absorbidas
- Evidencia:
  - `src/main/java/sv/com/jsoft/efactmh/util/RestUtil.java:84`
  - `src/main/java/sv/com/jsoft/efactmh/util/RestUtil.java:127`
  - `src/main/java/sv/com/jsoft/efactmh/filter/AuthorizationFilter.java:156`
  - `src/main/java/sv/com/jsoft/efactmh/filter/SecurityFilter.java` (captura general y solo log)
- Riesgo: NPE ocultos y diagnostico operativo dificil.
- Recomendacion: usar respuestas tipadas de error, no retornar `null`, y centralizar manejo de excepciones.

6. Cookies de aplicacion sin `Secure` explicito
- Evidencia:
  - `src/main/java/sv/com/jsoft/efactmh/util/JsfUtil.java:37`
  - `src/main/java/sv/com/jsoft/efactmh/util/JsfUtil.java:40`
- Observacion: en `web.xml` si esta `secure=true` para cookie de sesion (`src/main/webapp/WEB-INF/web.xml:14`), pero no para cookies personalizadas.
- Riesgo: en escenarios no forzados por TLS/proxy, cookies funcionales pueden viajar sin cifrado.
- Recomendacion: setear `cookie.setSecure(true)` condicionado a HTTPS y normalizar politicas de cookie.

7. Build Maven desactualizado e inconsistente
- Evidencia:
  - `pom.xml:141` (`maven-compiler-plugin` 3.1)
  - `pom.xml:153` (`maven-war-plugin` 2.3)
  - `pom.xml:174` (`javaee-endorsed-api`)
  - `pom.xml` usa `mapstruct` 1.6.3 y `mapstruct-processor` 1.5.2.Final
- Riesgo: incompatibilidades futuras, warnings, mantenimiento complejo.
- Recomendacion: alinear versiones de MapStruct y actualizar plugins Maven a versiones modernas.

### Baja prioridad

8. Inconsistencias de nomenclatura y codigo muerto
- Evidencia:
  - `Invoce*` (typo recurrente en nombre de clase/servicio/view)
  - `src/main/java/sv/com/jsoft/efactmh/view/InvoceView.java:755` (metodo vacio `requiereFactura`)
  - `ClientDao#getImagenesPorSolicitud` no refleja su funcion real.
- Riesgo: costo cognitivo y errores por ambiguedad.
- Recomendacion: estandarizar naming (`Invoice`) y limpiar metodos sin uso.

## 4) Fortalezas detectadas

- Uso de `@PostConstruct`, CDI y separacion basica por capas (`view/services/repository`).
- Buen uso de bindeo parametrizado en consultas JDBI (reduce riesgo de SQL injection).
- Configuracion de sesion con `http-only` y `secure` para cookie de sesion en `web.xml`.
- Registro de logs en puntos clave de flujo.

## 5) Riesgos de operacion

- Dependencia fuerte de servicios externos (firmador/MH) sin estrategia clara de resiliencia/reintentos centralizados.
- Timeouts HTTP cortos y heterogeneos (3/5/6s) sin configuracion central.
- Dificultad para monitoreo funcional por ausencia de pruebas y por manejo de errores no uniforme.

## 6) Plan recomendado (30 dias)

1. Semana 1: Estabilizacion minima
- externalizar endpoints y secretos,
- unificar datasource (`EfactDS`/`FacturaDS`),
- eliminar retornos `null` en cliente HTTP.

2. Semana 2: Calidad base
- crear set inicial de tests unitarios (calculo de totales/impuestos),
- agregar tests de integracion para repositorios criticos.

3. Semana 3: Refactor funcional
- extraer flujo de `preSave()` de `InvoceView` a servicio de aplicacion,
- reducir acoplamiento UI-logica de negocio.

4. Semana 4: Build y hardening
- actualizar plugins Maven,
- alinear versiones de dependencias,
- endurecer politicas de cookies y sesion.

## 7) Limitaciones del analisis

- Analisis estatico del codigo y configuracion local.
- No fue posible ejecutar build con Maven en este entorno porque `mvn` no esta instalado en PATH.

