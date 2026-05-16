package ro.tuiasi.ac.service;

import com.google.genai.Client;
import com.google.genai.types.CountTokensConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerateImagesConfig;
import com.google.genai.types.GenerationConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

public class GeminiClientServiceTests {
	private final GeminiClientService service = new GeminiClientService();
	private Client client = new Client();

	@Test
	void testGenerateWithMultiplicationExample() {
		ReflectionTestUtils.setField(service, "isMockMode", true);

		PromptBuilderService promptBuild = new PromptBuilderService();
		Path path = Path.of("src", "project", "inmultire.java");	
		
		String sourceCode = "package project " + "public class MultiplicationExample {\n"
				+ "    public static void main(String[] args) {\n" + "        int a = 5;\n" + "        int b = 4;\n"
				+ "        int result = 0;\n" + "        for (int i = 0; i < b; i++) {\n" + "            result += a;\n"
				+ "        }\n" + "        System.out.println(result);\n" + "    }\n" + "}";

		String prompt = promptBuild.FirstOptimizationPrompt(path, sourceCode);

		String response = service.generate(prompt);

		assertNotNull(response, "Răspunsul nu trebuie să fie null");

		assertTrue(response.contains("public class OptimizedCode"),
				"Răspunsul mock ar trebui să returneze clasa optimizată simulată.");

		assertTrue(response.startsWith("```java"),
				"Codul returnat trebuie să fie într-un bloc markdown java[cite: 135].");
	}

}
