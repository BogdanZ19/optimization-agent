package ro.tuiasi.ac.model;

import java.util.List;

public record OptimizationReport(List<OptimizationSuggestion> suggestions, int totalAnalyzed, int totalAccepted,
		int totalRejected, List<String> errors) {
}
