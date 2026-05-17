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

@RestController
@RequestMapping("/api/optimization")
public class OptimizationController {
	@Autowired
	private AgentLoopService agentLoopService;

	@PostMapping("/analyze")
	public ResponseEntity<OptimizationSuggestion> analyze(@RequestBody OptimizationRequest request) throws IOException {


		OptimizationSuggestion suggestion = agentLoopService.analyze(request.filePath());
		System.out.println(suggestion.optimizedCode());
		System.out.println("merge");
		return ResponseEntity.ok(suggestion);
	}

	@PostMapping("/accept")
	public ResponseEntity<String> accept(@RequestBody OptimizationSuggestion suggestion) throws IOException {
		(new PatchApplyService()).applyPatch(suggestion.filePath(), suggestion.optimizedCode());

		return ResponseEntity.ok("Optimization applied successfully.");
	}

	@PostMapping("/reject")
	public ResponseEntity<String> reject(@RequestBody OptimizationSuggestion suggestion) {
		return ResponseEntity.ok("Optimization rejected.");
	}
}
