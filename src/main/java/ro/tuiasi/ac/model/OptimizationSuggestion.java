package ro.tuiasi.ac.model;

import java.nio.file.Path;

public record OptimizationSuggestion(Path filePath, String originalCode, String optimizedCode, boolean accepted) {
}
