package ro.tuiasi.ac.service;

import org.junit.jupiter.api.Test;

import com.google.genai.Client;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

class PromptBuilderServiceTests {
	private Client client= new Client();

    private final PromptBuilderService promptBuilder = new PromptBuilderService();

    @Test
    void testFirstOptimizationPromptContainsKeyElements() {
        Path path = Path.of("src/main/java/Main.java");
        String code = "public class Main { }";

        String result = promptBuilder.FirstOptimizationPrompt(path, code);

        assertTrue(result.contains(code), "Promptul trebuie să conțină codul sursă original.");
        assertTrue(result.contains("Preserve package declaration"), "Trebuie să includă regula de păstrare a pachetului.");
        assertTrue(result.contains("Return the full optimized Java file"), "Trebuie să ceară fișierul complet, nu un patch.");
        assertTrue(result.contains("markdown code block"), "Trebuie să specifice formatul de returnare markdown.");
    }
    @Test
    void testLoopOptimizationPromptContainsErrorsAndCode() {
    	Path path = Path.of("src/main/java/Main.java");
        String originalCode = "public class Main { }";
        String previousCode = "public class Main { // error }";
        String errors = "Syntax error on line 1";

        String result = promptBuilder.LoopOptimizationPrompt(path, originalCode, previousCode, errors);

        assertTrue(result.contains(errors), "Promptul de revizuire trebuie să includă erorile de validare.");
        assertTrue(result.contains(originalCode), "Trebuie să conțină codul original pentru referință.");
        assertTrue(result.contains(previousCode), "Trebuie să conțină varianta optimizată anterior care a eșuat.");
        assertTrue(result.contains("Fix the optimized Java code"), "Trebuie să conțină instrucțiunea de corectare.");
    }

}
