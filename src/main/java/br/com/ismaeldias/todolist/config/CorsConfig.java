package br.com.ismaeldias.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permite o frontend na Vercel
        config.setAllowedOrigins(Arrays.asList(
            "https://ignite-todo-app-gold.vercel.app",
            "http://localhost:3000", // para desenvolvimento local
            "http://localhost:5173"  // Vite/React dev server
        ));
        
        // Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Headers permitidos (incluindo Authorization para Basic Auth)
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // Permite envio de credenciais (cookies, Authorization header)
        config.setAllowCredentials(true);
        
        // Expõe headers customizados ao frontend (se necessário)
        config.setExposedHeaders(Arrays.asList("Authorization"));
        
        // Aplica a todas as rotas
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
