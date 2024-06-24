package com.example.budgettrackerv1.config;

import com.example.budgettrackerv1.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(Constants.ALLOWED_ORIGIN)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*"); // evil
    }
}
