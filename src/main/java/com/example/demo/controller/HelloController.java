package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${spring.application.name:springboot-aws-docker-demo}")
    private String applicationName;

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Spring Boot 4 läuft erfolgreich.");
        response.put("application", applicationName);
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("deploymentTarget", "AWS Elastic Beanstalk Docker");
        return response;
    }

    @GetMapping("/api/hello")
    public Map<String, Object> hello() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Hallo von Spring Boot 4 + Maven + Docker");
        response.put("application", applicationName);
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", "OK");
        return response;
    }

    @GetMapping("/api/version")
    public Map<String, String> version() {
        Map<String, String> response = new HashMap<>();
        response.put("application", applicationName);
        response.put("version", "1.0.0");
        return response;
    }
    
    
    
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", "UP");
        return response;
    }
}
