package com.sih.certificate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Serve QR files saved in project root folder: ./qrcodes/
    // URL will be: http://localhost:8081/qrcodes/<file>.png
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/qrcodes/**")
                .addResourceLocations("file:qrcodes/");
    }
}
