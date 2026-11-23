# RegistroPlataforma - Backend (Entrega 7 y Entrega 8)

Servicios REST con persistencia para la plataforma de cursos virtuales (el frontend es la app Android `RegistroPlataforma`).

- Java 8 (Corretto 8 o cualquier JDK 1.8)
- Spring Boot 2.7.18 (ultima linea que soporta Java 8)
- Spring Data JPA + Hibernate, base de datos H2 en archivo (no hay que instalar nada)
- Documentacion interactiva con Swagger UI
- Coleccion de Postman para la demostracion

## 1. Requisitos

1. JDK 8. Compruebe con `java -version`: debe decir `1.8.0_xxx`.
2. Maven 3.6 o superior (o un IDE que lo traiga: IntelliJ IDEA o NetBeans). Compruebe con `mvn -version`.
   - Si `mvn -version` muestra otra version de Java, defina `JAVA_HOME` apuntando al JDK 8.
3. Postman (para la demostracion).
4. Git (para subir el codigo a GitHub).

## 2. Ejecutar el servidor

### Opcion A (Windows, recomendada): doble clic en `iniciar.bat`

El script detecta solo el Java y el Maven instalados, compila el proyecto y arranca el servidor. Compila con el JDK que tenga (genera codigo Java 8) y ejecuta con el `java` del PATH, que debe ser Java 8. La proxima vez, `ejecutar.bat` arranca sin recompilar.

### Opcion B: desde una terminal

1. Abra una terminal (CMD) en la carpeta del proyecto (la que contiene `pom.xml`).
2. Ejecute:

```
mvn spring-boot:run
```

   En Windows tambien puede hacer doble clic en `iniciar.bat`.
3. La primera vez Maven descarga las dependencias (unos minutos). Cuando aparezca `Started PlataformaApplication`, el servidor esta listo.
4. Compruebe en el navegador: http://localhost:8080/api/roles debe mostrar los roles DOCENTE y ESTUDIANTE.

### Opcion C: desde IntelliJ IDEA

1. File > Open y seleccione la carpeta del proyecto (la del `pom.xml`). Espere a que importe Maven.
2. File > Project Structure > Project > SDK: elija el JDK 8.
3. Abra `PlataformaApplication.java` y pulse el triangulo verde junto a `main`.

### Opcion D: desde NetBeans

1. File > Open Project y seleccione la carpeta del proyecto.
2. Clic derecho sobre el proyecto > Run.

## 3. Direcciones utiles

| Que | URL |
| --- | --- |
| API | http://localhost:8080/api/... |
| Swagger UI (probar desde el navegador) | http://localhost:8080/swagger-ui.html |
| Consola de la base de datos | http://localhost:8080/h2-console |

Datos para la consola H2: JDBC URL `jdbc:h2:file:./data/plataforma`, usuario `sa`, contrasena vacia.

La base de datos se guarda en la carpeta `data/` (se crea sola). Para empezar de cero, detenga el servidor y borre esa carpeta.

## 4. Demostracion con Postman

1. Postman > Import > seleccione `postman/RegistroPlataforma.postman_collection.json`.
2. Con el servidor encendido, abra la coleccion > boton Run > Run collection.
3. Se ejecutan en orden todas las peticiones y cada una valida su resultado (pestana Test Results). Las carpetas 00 a 13 cubren todo lo que pide la entrega.
4. Tambien puede ejecutar las peticiones una por una durante la sustentacion; las carpetas guardan los ids que la siguiente necesita.

Importante: ejecute siempre desde la carpeta `00` (genera un sufijo nuevo para los correos, por eso se puede repetir la demo sin reiniciar la base de datos).

## 5. Que cubre la entrega

| Requisito | Donde |
| --- | --- |
| CRUD oferta de educacion virtual | `/api/ofertas` |
| CRUD roles (docente, estudiante) | `/api/roles` y `/api/usuarios` |
| CRUD cursos | `/api/cursos` |
| CRUD unidades tematicas | `/api/unidades` |
| CRUD actividades del curso | `/api/actividades` |
| CRUD evaluacion de curso | `/api/evaluaciones-curso` |
| Procesos formativos | `/api/matriculas`, `/api/entregas` |
| Evaluacion y retroalimentacion | `PUT /api/entregas/{id}/calificacion` |
| Creacion de cursos | `POST /api/cursos` (reglas de negocio) y `POST /api/cursos/{id}/duplicar` |
| Seguimiento de progreso | `/api/seguimiento/...` |
| Estadisticas | `/api/estadisticas/...` |

El detalle de endpoints, la arquitectura y las decisiones estan en `docs/ARQUITECTURA.md`.

## 6. Subir el codigo a GitHub

1. Cree un repositorio vacio en GitHub (sin README ni .gitignore), por ejemplo `registroplataforma-backend`.
2. En la carpeta del proyecto:

```
git init
git add .
git commit -m "Entrega 7: backend REST de RegistroPlataforma"
git branch -M main
git remote add origin https://github.com/SU_USUARIO/registroplataforma-backend.git
git push -u origin main
```

3. Copie la URL del repositorio y peguela en Moodle.

El archivo `.gitignore` ya excluye `target/` y `data/`.

## 7. Problemas frecuentes

| Sintoma | Solucion |
| --- | --- |
| `mvn` no se reconoce | Instale Maven y agregue su carpeta `bin` al PATH, o use IntelliJ/NetBeans |
| `invalid target release` o `Unsupported class version` | Maven esta usando otro Java. Revise `mvn -version` y `JAVA_HOME` |
| `Port 8080 was already in use` | Cierre lo que use el puerto, o cambie `server.port` en `application.properties` |
| 400 en Postman con `JSON invalido` | Revise que el Body sea raw > JSON y que las fechas sean `2026-12-31T23:59:00` |
| Error de dependencias al compilar | Revise su conexion a internet (Maven descarga las librerias la primera vez) |


---

# Entrega 8 (Parte 2): roles Coordinador, Docente y Estudiante

## 8. Que se agrego

| Rol | Funcionalidad | Endpoints principales |
|---|---|---|
| Coordinador | Publicar curso con objetivos, resultados, cupos y fechas | `PUT /api/cursos/{id}/publicacion`, `PUT /api/cursos/{id}/despublicar` |
| Coordinador | Asignar curso a docente (notifica al docente) | `PUT /api/cursos/{id}/docente` |
| Coordinador | Reportes y estadisticas | `GET /api/reportes/plataforma`, `GET /api/estadisticas/...` |
| Coordinador / Docente | Enviar notificaciones | `POST /api/notificaciones/enviar` |
| Todos | Catalogo de cursos publicados con cupos disponibles | `GET /api/catalogo` |
| Docente | Configurar curso (porcentajes, secciones, pendientes) | `GET /api/cursos/{id}/configuracion` |
| Docente | Publicar contenidos y subir archivos | `/api/contenidos` |
| Docente | Actividades (taller, evaluacion, tarea), unidades | `/api/actividades`, `/api/unidades` |
| Docente | Evaluacion y retroalimentacion | `PUT /api/entregas/{id}/calificacion` |
| Docente | Seguimiento de progreso | `/api/seguimiento/...` |
| Docente / Estudiante | Foros y chat | `/api/foros`, `/api/foros/{id}/mensajes` |
| Docente | Procesos de formacion (actividades en secuencia) | `/api/procesos` |
| Docente | Atencion de reclamos | `/api/reclamos`, `PUT /api/reclamos/{id}/respuesta` |
| Estudiante | Inscripcion y retiro | `POST /api/matriculas`, `POST /api/matriculas/retiro` |
| Estudiante | Entregar actividades con archivo, descargar material | `POST /api/entregas/archivo`, `GET /api/contenidos/{id}/archivo` |
| Estudiante | Consultar porcentajes y notas | `GET /api/seguimiento/cursos/{c}/estudiantes/{e}` |
| Estudiante | Reclamos y certificados | `POST /api/reclamos`, `POST /api/certificados` |

Reglas nuevas: un curso solo admite matriculas si esta **publicado**; los cupos del curso se suman a los de la oferta; el retiro se bloquea si el curso termino o ya hay certificado; un reclamo debe hacerse en maximo 10 dias y, si se acepta, recalcula la nota final; el certificado solo se emite con curso aprobado (nota final 3.0 o mas) y tiene un codigo verificable `CERT-XXXXXXXXXXXX`.

Archivos: se guardan en la carpeta `archivos/` (maximo 10 MB; extensiones permitidas: pdf, doc(x), xls(x), ppt(x), txt, csv, zip, png, jpg...).

Identificacion: no hay token. El usuario que actua se envia como id (`coordinadorId`, `docenteId`, `estudianteId`, `usuarioId`) y el servidor valida su rol (403 si no corresponde). JWT queda como mejora para la Parte 3.

## 9. Actualizar desde la Parte 1

1. Detenga el servidor (Ctrl+C).
2. Reemplace el contenido de la carpeta del proyecto por el de este zip.
3. **Borre la carpeta `data`** para una demo limpia.
4. Ejecute `iniciar.bat` desde CMD.
5. En Postman importe de nuevo `postman/RegistroPlataforma.postman_collection.json`.

## 10. Demostracion con Postman (Parte 2)

Ejecute las carpetas **00 a 19 en orden** (clic derecho en la carpeta > Run, o Send una por una). Cada peticion tiene pruebas automaticas.
La carpeta **20 Archivos** se hace a mano: en Body > form-data, fila `archivo`, elija un archivo con *Select Files* y pulse Send. Luego ejecute la carpeta **21**.

Orden del flujo: crear usuarios y curso (00-06) > contenidos y configuracion (07) > coordinador publica (08) > estudiantes se inscriben (09) > proceso de formacion (10) > entregas y notas (11-13) > reclamos (14) > certificado (15) > foros (16) > notificaciones (17) > evaluacion del curso, reportes (18-19) > archivos (20) > retiro y limpieza (21).
