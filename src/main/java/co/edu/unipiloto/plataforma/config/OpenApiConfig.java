package co.edu.unipiloto.plataforma.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/** Titulo y descripcion de la documentacion interactiva (http://localhost:8080/swagger-ui.html). */
@Configuration
@OpenAPIDefinition(info = @Info(
        title = "API RegistroPlataforma",
        version = "1.0.0",
        description = "Servicios REST de la plataforma de cursos virtuales: ofertas, roles, usuarios, cursos, "
                + "unidades, actividades, matriculas, entregas, evaluacion de cursos, seguimiento de progreso y estadisticas."))
public class OpenApiConfig {
}
