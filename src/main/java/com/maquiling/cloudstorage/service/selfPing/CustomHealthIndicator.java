//package com.maquiling.cloudstorage.service.selfPing;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.boot.actuate.health.Health;
//import org.springframework.boot.actuate.health.HealthIndicator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//public class CustomHealthIndicator implements HealthIndicator {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public Health health() {
//        // Perform custom health check logic
//        boolean isHealthy = checkSomeService();
//
//        if (isHealthy) {
//            return Health.up().withDetail("service", "Available").build();
//        } else {
//            return Health.down().withDetail("service", "Unavailable").build();
//        }
//    }
//
//    private boolean checkSomeService() {
//        try {
//            String url = "http://localhost:8080/actuator/health"; // Replace with your application's health endpoint URL
//            String jsonResponse = restTemplate.getForObject(url, String.class);
//            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
//            String status = jsonNode.path("status").asText();
//            return "UP".equalsIgnoreCase(status);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
