package ro.tuiasi.ac.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import ro.tuiasi.ac.model.OptimizationRequest;
import ro.tuiasi.ac.model.OptimizationSuggestion;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class BackendApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/optimization";
    private static final String HEALTH_URL = "http://localhost:8080/api/health";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public BackendApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public boolean health() {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(HEALTH_URL)).GET().build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200 && (response.body().equalsIgnoreCase("true") || response.body().contains("Backend is running"));

        } catch (Exception e) {
            return false;
        }
    }

    public OptimizationSuggestion analyze(Path filePath) throws IOException, InterruptedException {
        OptimizationRequest optimizationRequest = new OptimizationRequest(filePath);

        String requestBody = objectMapper.writeValueAsString(optimizationRequest);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/analyze")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Analyze failed: " + response.body());
        }

        return objectMapper.readValue(response.body(), OptimizationSuggestion.class);
    }

    public String accept(OptimizationSuggestion suggestion) throws IOException, InterruptedException {
        return sendSuggestion("/accept", suggestion);
    }

    public String reject(OptimizationSuggestion suggestion) throws IOException, InterruptedException {
        return sendSuggestion("/reject", suggestion);
    }

    private String sendSuggestion(String endpoint, OptimizationSuggestion suggestion) throws IOException, InterruptedException {

        String requestBody = objectMapper.writeValueAsString(suggestion);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + endpoint)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Request failed: " + response.body());
        }

        return response.body();
    }
}
