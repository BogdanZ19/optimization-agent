package ro.tuiasi.ac.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for health check endpoints.
 * Provides endpoints to verify the backend service is running.
 */
@RestController
public class HealthController {

    /**
     * Health check endpoint.
     * Returns a simple message indicating the backend is operational.
     *
     * @return a ResponseEntity with HTTP 200 status and a confirmation message
     */
    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Backend is running");
    }
}
