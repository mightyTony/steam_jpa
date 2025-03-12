package com.example.steam.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${origin.url}")
    private String ALLOWED_ORIGINS;

    //FIXME : 나중에 헤더 제한
    private static final List<String> ALLOWED_HEADERS = List.of("*");

    private static final List<String> ALLOWED_METHODS = List.of("GET","POST","PUT", "DELETE", "OPTIONS");
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(ALLOWED_ORIGINS));
        config.setAllowedHeaders(ALLOWED_HEADERS);
        config.setAllowedMethods(ALLOWED_METHODS);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter();
    }
}
