package ro.tuiasi.ac.model;
import java.nio.file.Path;
public record FileChange(Path filePath,
		String oldContent,
		String newContent,
		boolean isApplied) {}
