package ro.tuiasi.ac.model;

import java.nio.file.Path;

/**
 * Represents a change made to a file, including the file path, the
 * original content, the updated content, and whether the change has been applied.
 *
 * @param filePath the path of the file being changed
 * @param oldContent the content of the file before the change
 * @param newContent the content of the file after the change
 * @param isApplied {@code true} when the change has been applied, {@code false} otherwise
 */
public record FileChange(
        Path filePath,
        String oldContent,
        String newContent,
        boolean isApplied) {}

