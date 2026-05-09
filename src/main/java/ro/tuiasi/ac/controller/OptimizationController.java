package ro.tuiasi.ac.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.tuiasi.ac.model.OptimizationRequest;
import ro.tuiasi.ac.model.OptimizationSuggestion;
import ro.tuiasi.ac.service.PatchApplyService;

import java.io.IOException;

@RestController
@RequestMapping("/api/optimization")
public class OptimizationController {

	@PostMapping("/analyze")
	public ResponseEntity<OptimizationSuggestion> analyze(@RequestBody OptimizationRequest request) {
		OptimizationSuggestion suggestion = null;
		//OptimizationSuggestion suggestion = agentLoopService.analyze(filePath);
		return ResponseEntity.ok(suggestion);
	}

	@PostMapping("/accept")
	public ResponseEntity<String> accept(@RequestBody OptimizationSuggestion suggestion) throws IOException {
		PatchApplyService.applyPatch(suggestion.filePath(), suggestion.optimizedCode());

		return ResponseEntity.ok("Optimization applied successfully.");
	}

	@PostMapping("/reject")
	public ResponseEntity<String> reject(@RequestBody OptimizationSuggestion suggestion) {
		return ResponseEntity.ok("Optimization rejected.");
	}
}
