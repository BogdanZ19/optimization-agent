
package ro.tuiasi.ac.agent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ro.tuiasi.ac.model.*;
import ro.tuiasi.ac.service.*;


@ExtendWith(MockitoExtension.class)
class AgentLoopServiceTest {


    @Mock
    private GeminiClientService geminiClient;
    @Mock
    private PromptBuilderService promptBuilder;
    @Mock
    private CodeValidationService validator;
    @Mock
    private CodeReaderService codeReader;

    @InjectMocks
    private AgentLoopService agentLoopService;

    private final Path filePath = Paths.get("src/main/java/Example.java");
    private final String originalCode = "public class Example {}";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(agentLoopService, "maxIterations", 3);
        ReflectionTestUtils.setField(agentLoopService, "size", 1024);
    }

    @Test
    void testAnalyze_SuccessOnFirstTry() throws IOException {
        
        String firstPrompt = "Optimizes this code please";
        String idealProposal = "public class Example { // optimized }";
        
        ValidationResult mockResult = mock(ValidationResult.class);
        when(mockResult.isValid()).thenReturn(true);
        when(mockResult.getErrors()).thenReturn(Collections.emptyList());

        when(codeReader.fileRead(filePath)).thenReturn(originalCode);
        when(promptBuilder.FirstOptimizationPrompt(filePath, originalCode)).thenReturn(firstPrompt);
        when(geminiClient.generate(firstPrompt)).thenReturn(idealProposal);
        when(validator.validate(originalCode, idealProposal, filePath)).thenReturn(mockResult);

        
        OptimizationSuggestion result = agentLoopService.analyze(filePath);

        
        assertNotNull(result);
        assertEquals(filePath, result.filePath()); 
        assertEquals(idealProposal, result.optimizedCode()); 
        
       
        verify(promptBuilder, times(1)).FirstOptimizationPrompt(any(), any());
        verify(promptBuilder, never()).LoopOptimizationPrompt(any(), any(), any(), any());
    }

    @Test
    void testAnalyze_FailsThenSucceeds() throws IOException {
       
        String firstPrompt = "Initial prompt";
        String loopPrompt = "Loop prompt";
        String badProposal = "public class Bad {}";
        String goodProposal = "public class Good {}";

        
        ValidationResult badResult = mock(ValidationResult.class);
        when(badResult.isValid()).thenReturn(false);
        when(badResult.getErrors()).thenReturn(List.of("Syntax Error"));

        
        ValidationResult goodResult = mock(ValidationResult.class);
        when(goodResult.isValid()).thenReturn(true);
        when(goodResult.getErrors()).thenReturn(Collections.emptyList());

        when(codeReader.fileRead(filePath)).thenReturn(originalCode);
        
      
        when(promptBuilder.FirstOptimizationPrompt(filePath, originalCode)).thenReturn(firstPrompt);
        when(geminiClient.generate(firstPrompt)).thenReturn(badProposal);
        when(validator.validate(originalCode, badProposal, filePath)).thenReturn(badResult);

      
        when(promptBuilder.LoopOptimizationPrompt(filePath, originalCode, badProposal, "Syntax Error")).thenReturn(loopPrompt);
        when(geminiClient.generate(loopPrompt)).thenReturn(goodProposal);
        when(validator.validate(originalCode, goodProposal, filePath)).thenReturn(goodResult);

    
        OptimizationSuggestion result = agentLoopService.analyze(filePath);

     
        assertNotNull(result);
        assertEquals(goodProposal, result.optimizedCode());
        
    
        verify(promptBuilder, times(1)).FirstOptimizationPrompt(any(), any());
        verify(promptBuilder, times(1)).LoopOptimizationPrompt(any(), any(), any(), any());
    }
}