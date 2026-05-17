package ro.tuiasi.ac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
@SpringBootTest
public class GeminiClientServiceTests {
	@Autowired
	private GeminiClientService service;
	@Autowired
	private PromptBuilderService promptBuild;
	
	@Test
	void testGenerateWithMultiplicationExample() {

		Path path = Path.of("src", "project", "inmultire.java");	
		
		String sourceCode = "package project " + "public class MultiplicationExample {\n"
				+ "    public static void main(String[] args) {\n" + "        int a = 5;\n" + "        int b = 4;\n"
				+ "        int result = 0;\n" + "        for (int i = 0; i < b; i++) {\n" + "            result += a;\n"
				+ "        }\n" + "        System.out.println(result);\n" + "    }\n" + "}";

		String prompt = promptBuild.FirstOptimizationPrompt(path, sourceCode);

		String response = service.generate(prompt);
		System.out.println(response);

		assertNotNull(response, "Răspunsul nu trebuie să fie null");

		assertTrue(response.startsWith("```java"),
				"Codul returnat trebuie să fie într-un bloc markdown java[cite: 135].");
	}

}
