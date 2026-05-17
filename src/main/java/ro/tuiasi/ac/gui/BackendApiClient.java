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

/**
 * Client for communicating with the backend optimization API.
 * <p>
 * This class provides methods to verify backend health, send files for analysis,
 * and accept or reject optimization suggestions.
 */
public class BackendApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/optimization";
    private static final String HEALTH_URL = "http://localhost:8080/api/health";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new backend API client using the default HTTP client and JSON mapper.
     */
    public BackendApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Checks whether the backend service is reachable and healthy.
     *
     * @return {@code true} if the backend responds with a successful health status, {@code false} otherwise
     */
    public boolean health() {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(HEALTH_URL)).GET().build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200 && (response.body().equalsIgnoreCase("true") || response.body().contains("Backend is running"));

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sends a file path to the backend for analysis.
     *
     * @param filePath the path of the file to analyze
     * @return the optimization suggestion returned by the backend
     * @throws IOException if an I/O error occurs while communicating with the backend
     * @throws InterruptedException if the request is interrupted
     */
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

    /**
     * Sends an optimization suggestion acceptance request to the backend.
     *
     * @param suggestion the suggestion to accept
     * @return the backend response body
     * @throws IOException if an I/O error occurs while communicating with the backend
     * @throws InterruptedException if the request is interrupted
     */
    public String accept(OptimizationSuggestion suggestion) throws IOException, InterruptedException {
        return sendSuggestion("/accept", suggestion);
    }

    /**
     * Sends an optimization suggestion rejection request to the backend.
     *
     * @param suggestion the suggestion to reject
     * @return the backend response body
     * @throws IOException if an I/O error occurs while communicating with the backend
     * @throws InterruptedException if the request is interrupted
     */
    public String reject(OptimizationSuggestion suggestion) throws IOException, InterruptedException {
        return sendSuggestion("/reject", suggestion);
    }

    /**
     * Sends a suggestion request to the specified endpoint.
     *
     * @param endpoint the backend endpoint to call (for example, {@code "/accept"} or {@code "/reject"})
     * @param suggestion the optimization suggestion to send
     * @return the backend response body
     * @throws IOException if an I/O error occurs while communicating with the backend
     * @throws InterruptedException if the request is interrupted
     */
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
