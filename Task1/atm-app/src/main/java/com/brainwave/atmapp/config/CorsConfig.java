package com.brainwave.atmapp.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    private final String allowedOrigins;
    private final String allowedMethods;
    private final String allowedHeaders;
    private final boolean allowCredentials;

    public CorsConfig(Dotenv dotenv) {
        this.allowedOrigins = dotenv.get("ALLOWEDORIGINS");;
        this.allowedMethods = dotenv.get("ALLOWEDMETHODS");
        this.allowedHeaders = dotenv.get("ALLOWEDHEADERS");
        this.allowCredentials = Boolean.parseBoolean(dotenv.get("ALLOWCREDENTIALS"));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowedMethods(allowedMethods.split(","))
                        .allowedHeaders(allowedHeaders.split(","))
                        .allowCredentials(allowCredentials);
            }
        };
    }
}
