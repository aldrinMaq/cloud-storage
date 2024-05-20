package com.maquiling.cloudstorage.service.selfPing;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SelfPingTask {

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 600000) // Every 10 minutes
    public void pingSelf() {
        try {
            String url = "https://cloud-storage-g8vy.onrender.com/actuator/health"; // Replace with your application's URL
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}