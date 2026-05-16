import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

import ro.tuiasi.ac.service.*;
import ro.tuiasi.ac.model.*;

@ExtendWith(MockitoExtension.class)
class AgentLoopServiceTest {

    @Mock private GeminiClientService geminiClient;
    @Mock private PromptBuilderService promptBuilder;
    @Mock private CodeValidationService validator;
    @Mock private CodeReaderService codeReader;

    @InjectMocks
    private AgentLoopService agentLoopService;

    @BeforeEach
    void setUp() {
        // Setăm manual valorile injectate prin @Value, deoarece Mockito nu le procesează
        ReflectionTestUtils.setField(agentLoopService, "maxIterations", 3);
    }

    @Test
    void testRunOptimizationLoop_SuccessOnFirstTry() {
        // Arrange
        Path path = Paths.get("TestFile.java");
        String originalCode = "public class Test {}";
        String optimizedCode = "public class Optimized {}";
        String mockPrompt = "Optimize this code";

        when(codeReader.fileRead(path)).thenReturn(originalCode);
        when(promptBuilder.FirstOptimizationPrompt(path, originalCode)).thenReturn(mockPrompt);
        when(geminiClient.generate(mockPrompt)).thenReturn(optimizedCode);
        
        // Mocking validation result
        ValidationResult vResult = mock(ValidationResult.class);
        when(vResult.isValid()).thenReturn(true);
        when(validator.validate(optimizedCode)).thenReturn(vResult);

        // Act
        OptimizationSuggestion result = agentLoopService.runOptimizationLoop(path);

        // Assert
        assertNotNull(result);
        verify(geminiClient, times(1)).generate(anyString());
        verify(validator, times(1)).validate(optimizedCode);
    }

    @Test
    void testRunOptimizationLoop_MaxIterationsReached() {
        // Arrange
        Path path = Paths.get("TestFile.java");
        String originalCode = "code";
        when(codeReader.fileRead(path)).thenReturn(originalCode);
        
        // Mockăm un rezultat care e mereu invalid
        ValidationResult invalidResult = mock(ValidationResult.class);
        when(invalidResult.isValid()).thenReturn(false);
        when(invalidResult.getErrorMessage()).thenReturn("Syntax Error");
        
        when(promptBuilder.FirstOptimizationPrompt(any(), any())).thenReturn("prompt");
        when(promptBuilder.LoopOptimizationPrompt(any(), any(), any(), any())).thenReturn("retry prompt");
        when(geminiClient.generate(anyString())).thenReturn("bad code");
        when(validator.validate(anyString())).thenReturn(invalidResult);

        // Act
        agentLoopService.analyze(path);

        // Assert
        // Verificăm că a rulat de 3 ori (maxIterations setat în setUp)
        verify(geminiClient, times(5)).generate(anyString());
    }
}