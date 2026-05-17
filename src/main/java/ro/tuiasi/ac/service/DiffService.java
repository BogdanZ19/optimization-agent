package ro.tuiasi.ac.service;

import org.springframework.stereotype.Service;

/**
 * Service component responsible for generating a simple textual diff report
 * between an original source and its optimized version.
 */
@Service
public class DiffService {
	/**
	 * Generates a summary report describing the line count difference
	 * between the original and optimized code snippets.
	 *
	 * @param originalCode the original source code, may be {@code null}
	 * @param optimizedCode the optimized source code, may be {@code null}
	 * @return a textual diff report containing line counts and a summary of
	 *         whether lines were added, removed, modified, or unchanged
	 */
	public String generateSimpleDiff(String originalCode, String optimizedCode) {
		if (originalCode == null) {
			originalCode = "";
		}
		if (optimizedCode == null) {
			optimizedCode = "";
		}
		int originalLinesCount = originalCode.isEmpty() ? 0 : originalCode.split("\r?\n").length;
		int optimizedLinesCount = optimizedCode.isEmpty() ? 0 : optimizedCode.split("\r?\n").length;

		int difference = optimizedLinesCount - originalLinesCount;

		String diffReport = "Modifications Summary:\n";
		diffReport += "-Original code lines: " + originalLinesCount + "\n";
		diffReport += "-Optimized code lines: " + optimizedLinesCount + "\n";

		if (difference > 0) {
			diffReport += "=> The AI added " + difference + " lines in total.";
		} else if (difference < 0) {
			diffReport += "=> The AI removed " + Math.abs(difference) + " lines in total.";
		} else {
			if (!originalCode.equals(optimizedCode)) {
				diffReport += "=> The number of lines is the same, but the content was modified.";
			} else {
				diffReport += "=> No modifications detected.";
			}
		}
		return diffReport;
	}
}
