package co.edu.unipiloto.plataforma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** Cifrado de contrasenas: nunca se guardan en texto plano. */
@Configuration
public class SeguridadConfig {

    @Bean
    public BCryptPasswordEncoder codificador() {
        return new BCryptPasswordEncoder();
    }
}
