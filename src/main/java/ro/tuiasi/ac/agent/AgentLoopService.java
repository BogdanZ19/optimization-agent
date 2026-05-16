package ro.tuiasi.ac.agent;

import java.io.File;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;

import ro.tuiasi.ac.service.*;
import ro.tuiasi.ac.agent.*;
import ro.tuiasi.ac.model.*;

@Service
public class AgentLoopService {
	private final GeminiClientService geminiClient;
	private final PromptBuilderService promptBuilder;
	private final CodeValidationService validator;
	private final CodeReaderService codeReader;

	@Value("${agent.max-iterations}")
	private int maxIterations;

	@Value("${scanner.max-file-size}")
	private int size;

	public AgentLoopService(GeminiClientService geminiClient, PromptBuilderService promptBuilder,
			CodeValidationService validator) {
		this.geminiClient = geminiClient;
		this.promptBuilder = promptBuilder;
		this.validator = validator;
	}

	public OptimizationSuggestion analyze(Path filePath) {
		String originalCode = codeReader.fileRead(filePath);
		AgentState state = new AgentState(originalCode);
		String currentProposal = "";
		for (int i = 1; i <= maxIterations; i++) {	
			String prompt = (i == 1) ? promptBuilder.FirstOptimizationPrompt(filePath, originalCode)
					: promptBuilder.LoopOptimizationPrompt(filePath, originalCode, currentProposal,
							state.getLastErrors());

			currentProposal = geminiClient.generate(prompt);

			ValidationResult vResult = validator.validate(currentProposal);

			AgentStep step = new AgentStep(i, (i == 1) ? "INITIAL_PROPOSAL" : "REVISION", currentProposal,
					vResult.isValid() ? "SUCCESS" : "FAILED", vResult.getErrorMessage());
			state.addStep(step);
			if (vResult.isValid()) {
				state.setFinalized(true);
				state.setFinalCode(currentProposal);
				break;
			} else {
				state.setLastErrors(vResult.getErrorMessage());
			}
		}
		OptimizationSuggestion sugestion = new OptimizationSuggestion(filePath, originalCode, currentProposal, false);
	}
}
