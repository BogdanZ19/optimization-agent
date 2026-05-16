package ro.tuiasi.ac.agent;

import java.io.IOException;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

import ro.tuiasi.ac.service.*;
import ro.tuiasi.ac.model.*;

@Service
public class AgentLoopService {
    @Autowired
	private GeminiClientService geminiClient;
    @Autowired
	private PromptBuilderService promptBuilder;
    @Autowired
	private CodeValidationService validator;
    @Autowired
	private CodeReaderService codeReader;
    @Autowired


	@Value("${agent.max-iterations}")
	private int maxIterations;

	@Value("${scanner.max-file-size}")
	private int size;


	public OptimizationSuggestion analyze(Path filePath) throws IOException {
		String originalCode = codeReader.fileRead(filePath);
		AgentState state = new AgentState(originalCode);
		String currentProposal = "";
		for (int i = 1; i <= maxIterations; i++) {
			String prompt = (i == 1) ? promptBuilder.FirstOptimizationPrompt(filePath, originalCode)
					: promptBuilder.LoopOptimizationPrompt(filePath, originalCode, currentProposal,
							state.getLastErrors());

			currentProposal = geminiClient.generate(prompt);

			ValidationResult vResult = validator.validate(originalCode, currentProposal, filePath);
			String errorMessages = String.join(";", vResult.getErrors());
			AgentStep step = new AgentStep(i, (i == 1) ? "INITIAL_PROPOSAL" : "REVISION", currentProposal,
					vResult.isValid() ? "SUCCESS" : "FAILED", errorMessages);

			state.addStep(step);
			if (vResult.isValid()) {
				state.setFinalized(true);
				state.setFinalCode(currentProposal);
				break;
			} else {

				state.setLastErrors(errorMessages);
			}
		}
		
		OptimizationSuggestion sugestion = new OptimizationSuggestion(filePath, originalCode, validator.cleanMarkdown(currentProposal), false);
		
		return sugestion;
	}
}
