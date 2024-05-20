package com.maquiling.cloudstorage.service.selfPing;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Perform custom health check logic
        boolean isHealthy = checkSomeService();

        if (isHealthy) {
            return Health.up().withDetail("service", "Available").build();
        } else {
            return Health.down().withDetail("service", "Unavailable").build();
        }
    }

    private boolean checkSomeService() {
        // Implement your custom health check logic here
        return true;
    }
}
