package ro.tuiasi.ac.service;

import java.nio.file.Path;
import org.springframework.stereotype.Service;

/**
 * Service responsible for constructing prompt strings for an AI-powered Java
 * code optimization agent.
 * <p>
 * This class provides standardized methods to generate instructions for a Large
 * Language Model (LLM),
 * handling both the initial optimization request and subsequent retry requests
 * if the generated
 * code fails validation checks.
 * </p>
 */
@Service
public class PromptBuilderService {

	/**
	 * Constructs the initial prompt used to request code optimization from the AI
	 * agent.
	 * <p>
	 * The generated prompt includes specific rules that the AI must follow, such as
	 * preserving package declarations, maintaining public APIs, adhering to core
	 * Java libraries,
	 * and returning the entire file wrapped in a markdown code block.
	 * </p>
	 *
	 * @param relativePath the relative {@link Path} of the original Java file being
	 *                     optimized
	 * @param sourceCode   the raw string content of the original Java source code
	 * @return a fully formatted string prompt ready to be sent to the AI model
	 */
	public String FirstOptimizationPrompt(Path relativePath, String sourceCode) {
		String prompt = new String(
				"You are a Java code optimization agent. You receive exactly one Java file, not a folder or full "
						+ "project. Improve performance and readability while preserving behavior. Rules: 1. Preserve "
						+ "package declaration. 2. Do not rename public classes, public methods, constructors, or public "
						+ "fields. 3. Return the full optimized Java file, not a partial patch. 4. Do not invent "
						+ "dependencies unless they are from the Java standard library. 5. Keep the code beginner-friendly. 6. Don't add any comments to explain the changes "
						+ "Return the full optimized Java code inside one markdown "
						+ "code block. Java file path: " + relativePath + " Original code: ```" + sourceCode + " ```");
		return prompt;
	}

	/**
     * Constructs a follow-up prompt used when the previously generated code failed validation.
     * <p>
     * This prompt provides the AI agent with the context of its failure. It includes the original
     * code, the failed optimization attempt, and the specific validation errors (e.g., compiler errors 
     * or test failures) that the model needs to correct.
     * </p>
     *
     * @param relativePath     the relative {@link Path} of the original Java file
     * @param sourceCode       the raw string content of the original Java source code
     * @param optimizedCode    the previous AI-generated code that failed validation
     * @param validationErrors a formatted string detailing the compiler or test errors encountered
     * @return a formatted string prompt instructing the AI to fix the provided errors
     */
	public String LoopOptimizationPrompt(Path relativePath, String sourceCode, String optimizedCode,
			String validationErrors) {
		String prompt = new String(
				"The code you generated did not pass validation. Fix the optimized Java code according to these validation errors \n  "
						+ validationErrors + "  \n Original file path  " + relativePath + "  \n  Original code :\n "
						+ sourceCode + " \n Your previous optimized code : \n"
						+ optimizedCode + " \n Return only the completed corect java code inside one markdown block");
		return prompt;
	}
}
