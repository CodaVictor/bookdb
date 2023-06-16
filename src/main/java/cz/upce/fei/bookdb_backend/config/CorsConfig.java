package cz.upce.fei.bookdb_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173/", "http://127.0.0.1:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type", "Requestor-Type")
                .allowCredentials(true)
                .exposedHeaders("X-Get-Header")
                .maxAge(3600);


        /*
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("HEAD","GET", "POST", "PUT", "DELETE", "PATCH") // * - výchozí
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type") // * - výchozí
                .allowCredentials(true); // Povolení JWT tokenů

        registry.addMapping("/token/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

        registry.addMapping("/books/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

        registry.addMapping("/reviews/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

        registry.addMapping("/authors/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

        registry.addMapping("/categories/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

        registry.addMapping("/genres/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

        registry.addMapping("/publishers/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

        registry.addMapping("/users/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

        registry.addMapping("/user/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

        registry.addMapping("/role/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy

         */
    }
}
