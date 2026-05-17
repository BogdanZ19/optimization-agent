/**
 * Represents a request to optimize a specific file.
 *
 * <p>The request contains the path to the file that should be processed
 * by the optimization logic.</p>
 */
package ro.tuiasi.ac.model;
import java.nio.file.Path;

/**
 * A request object holding the path of the file to optimize.
 *
 * @param filePath the path of the file to optimize
 */
public record OptimizationRequest(Path filePath) {
}
