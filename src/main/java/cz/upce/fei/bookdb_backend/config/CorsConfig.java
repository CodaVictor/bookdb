package cz.upce.fei.bookdb_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
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

        registry.addMapping("/token/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // * - výchozí
                .allowedHeaders("*") // * - výchozí
                .allowCredentials(true) // Povolení JWT tokenů
                .maxAge(3600); // Nastavení maximální doby odezvy
    }
}
