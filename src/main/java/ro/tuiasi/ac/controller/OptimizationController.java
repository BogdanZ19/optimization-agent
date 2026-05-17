package ro.tuiasi.ac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.tuiasi.ac.model.OptimizationRequest;
import ro.tuiasi.ac.model.OptimizationSuggestion;
import ro.tuiasi.ac.service.PatchApplyService;
import ro.tuiasi.ac.agent.AgentLoopService;
import java.io.IOException;

/**
 * REST controller responsible for handling optimization-related operations.
 * <p>
 * Accepts optimization requests, delegates analysis to the agent loop service,
 * applies accepted patch suggestions, and rejects suggestions when needed.
 */
@RestController
@RequestMapping("/api/optimization")
public class OptimizationController {
	@Autowired
	private AgentLoopService agentLoopService;

	/**
	 * Analyzes a file for potential optimizations using the configured agent loop service.
	 *
	 * @param request the optimization request containing the file path to analyze
	 * @return a response entity containing the optimization suggestion
	 * @throws IOException if the analysis process encounters an I/O error
	 */
	@PostMapping("/analyze")
	public ResponseEntity<OptimizationSuggestion> analyze(@RequestBody OptimizationRequest request) throws IOException {
		OptimizationSuggestion suggestion = agentLoopService.analyze(request.filePath());
		System.out.println(suggestion.optimizedCode());
		System.out.println("merge");
		return ResponseEntity.ok(suggestion);
	}

	/**
	 * Applies the provided optimization suggestion by patching the target file.
	 *
	 * @param suggestion the optimization suggestion containing the file path and optimized code
	 * @return a response entity with a success message when the patch is applied
	 * @throws IOException if patch application encounters an I/O error
	 */
	@PostMapping("/accept")
	public ResponseEntity<String> accept(@RequestBody OptimizationSuggestion suggestion) throws IOException {
		(new PatchApplyService()).applyPatch(suggestion.filePath(), suggestion.optimizedCode());

		return ResponseEntity.ok("Optimization applied successfully.");
	}

	/**
	 * Rejects the provided optimization suggestion without applying any changes.
	 *
	 * @param suggestion the optimization suggestion to reject
	 * @return a response entity with a rejection confirmation message
	 */
	@PostMapping("/reject")
	public ResponseEntity<String> reject(@RequestBody OptimizationSuggestion suggestion) {
		return ResponseEntity.ok("Optimization rejected.");
	}
}
