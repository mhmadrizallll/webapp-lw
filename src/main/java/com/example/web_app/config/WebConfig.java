package com.example.web_app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Ambil path project root
        Path root = Paths.get("").toAbsolutePath().getParent();

        String externalPath = root.toUri().toString();

        registry.addResourceHandler("/docs/**")
                .addResourceLocations(externalPath);
    }
}