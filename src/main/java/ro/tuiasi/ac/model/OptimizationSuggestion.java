/**
 * Represents a suggestion for optimizing source code in a specific file.
 *
 * <p>This record encapsulates the location of the file being optimized, the
 * original code segment, the proposed optimized code segment, and whether the
 * suggestion has been accepted.</p>
 */
package ro.tuiasi.ac.model;
import java.nio.file.Path;

/**
 * A data container for optimization suggestions.
 *
 * @param filePath the path to the file containing the code to optimize
 * @param originalCode the original code before optimization
 * @param optimizedCode the optimized version of the original code
 * @param accepted whether the optimization suggestion has been accepted
 */
public record OptimizationSuggestion(Path filePath, String originalCode, String optimizedCode, boolean accepted) {
}
