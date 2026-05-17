package ro.tuiasi.ac.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import ro.tuiasi.ac.model.*;
import ro.tuiasi.ac.service.*;

import static org.junit.jupiter.api.Assertions.*;

class AgentLoopServiceTest {

    private AgentLoopService agentLoopService;
    
    
    private FakeGeminiClient geminiClient;
    private FakeValidator validator;

    @BeforeEach
    void setUp() throws Exception {
        agentLoopService = new AgentLoopService();

       
        CodeReaderService codeReader = new CodeReaderService();
        PromptBuilderService promptBuilder = new PromptBuilderService();
        
        geminiClient = new FakeGeminiClient();
        validator = new FakeValidator();

        injectField("codeReader", codeReader);
        injectField("promptBuilder", promptBuilder);
        injectField("geminiClient", geminiClient);
        injectField("validator", validator);

        injectField("maxIterations", 3);
        injectField("size", 5000);
    }

    @Test
    void testAnalyze_SuccessOnFirstIteration(@TempDir Path tempDir) throws IOException {
     
        Path testFile = tempDir.resolve("Test.java");
        String originalCode = "public class Test {}";
        Files.writeString(testFile, originalCode);

        String aiResponse = "```java\npublic class Test { // optimized }\n```";
        geminiClient.setResponse(aiResponse);
        validator.setValid(true);


        OptimizationSuggestion result = agentLoopService.analyze(testFile);


        assertNotNull(result);
        assertEquals("public class Test { // optimized }", result.optimizedCode());
        assertEquals(1, geminiClient.getCallCount(), "Trebuia să se oprească după prima iterație reușită.");
    }

    @Test
    void testAnalyze_SuccessAfterRetrying(@TempDir Path tempDir) throws IOException {

        Path testFile = tempDir.resolve("Test.java");
        Files.writeString(testFile, "public class Test {}");


        validator.setValid(false); 
        geminiClient.setResponse("```java\nInvalid Code\n```");


        OptimizationSuggestion result = agentLoopService.analyze(testFile);


        assertTrue(geminiClient.getCallCount() > 1, "Ar fi trebuit să încerce de mai multe ori.");
    }


    private void injectField(String fieldName, Object value) throws Exception {
        Field field = AgentLoopService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(agentLoopService, value);
    }



    private static class FakeGeminiClient extends GeminiClientService {
        private String response;
        private int callCount = 0;

        public void setResponse(String response) { this.response = response; }
        public int getCallCount() { return callCount; }

        @Override
        public String generate(String prompt) {
            callCount++;
            return response;
        }
    }

    private static class FakeValidator extends CodeValidationService {
        private boolean valid = true;

        public void setValid(boolean valid) { this.valid = valid; }

        @Override
        public ValidationResult validate(String originalCode, String currentProposal, Path filePath) {
            ValidationResult res = new ValidationResult(valid);
            if (!valid) {
                res.addError("Syntax Error");

                this.valid = true; 
            }
            return res;
        }

        @Override
        public String cleanMarkdown(String code) {
            return super.cleanMarkdown(code);
        }
    }
}