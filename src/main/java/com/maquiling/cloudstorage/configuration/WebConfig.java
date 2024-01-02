package com.maquiling.cloudstorage.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Adjust the path pattern to suit your needs
                .allowedOrigins(
                        "http://localhost:5173",
                        "https://your-cloud-maquiling.vercel.app",
                        "https://cloud-storage-frontend-swart.vercel.app",
                        "https://08f7-2001-4455-266-d800-e540-a8ee-6a3c-b78f.ngrok-free.app"
                ) .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}