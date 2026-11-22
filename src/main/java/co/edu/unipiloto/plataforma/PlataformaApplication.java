package co.edu.unipiloto.plataforma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del backend de RegistroPlataforma.
 * Al ejecutarla se levanta el servidor web embebido en http://localhost:8080
 */
@SpringBootApplication
public class PlataformaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlataformaApplication.class, args);
    }
}
