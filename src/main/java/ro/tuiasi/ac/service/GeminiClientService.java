package ro.tuiasi.ac.service;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class GeminiClientService {

	@Value("${gemini.api.key}")
	private String apiKey;

	@Value("${app.mock-mode}")
	private boolean isMockMode;

	@Value("${gemini.model}")
	private String model;

	private Client client;

	@PostConstruct
	public void init() {
		if (!isMockMode) {
			this.client = Client.builder()
					.apiKey(apiKey)
					.build();
		}
	}

	public String generate(String prompt) {
		if (isMockMode) {
			return generateMockResponse();
		}

		return callGeminiApi(prompt);
	}

	private String callGeminiApi(String prompt) {
		var response = client.models.generateContent(model, prompt, null);
		return response.text();
	}

	private String generateMockResponse() {
		return "```java\n" +
				"public class OptimizedCode {\n" +
				"    public static void main(String[] args) {\n" +
				"        System.out.println(\"Hello Optimized World\");\n" +
				"    }\n" +
				"}\n" +
				"```";
	}
}