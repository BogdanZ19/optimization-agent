package ro.tuiasi.ac.service;

import org.springframework.stereotype.Service;
import ro.tuiasi.ac.model.ValidationResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.tools.*;

@Service
public class CodeValidationService {
	public ValidationResult validate(String originalCode, String rawOptimizedCode, Path filePath) {
		String optimizedCode = cleanMarkdown(rawOptimizedCode);

		ValidationResult result = new ValidationResult(true);

		if (optimizedCode == null || optimizedCode.trim().isEmpty()) {
			result.addError("Error: The optimized code is empty or missing.");
			return result;
		}

		String originalPackage = findPattern(originalCode, "package\\s+([\\w\\.]+);");
		String optimizedPackage = findPattern(optimizedCode, "package\\s+([\\w\\.]+);");

		if (originalPackage != null && !originalPackage.equals(optimizedPackage)) {
			result.addError(
					"Rule violation: The package declaration was modified or deleted. Expected: " + originalPackage);
		}

		String originalClass = findPattern(originalCode, "public\\s+class\\s+(\\w+)");
		String optimizedClass = findPattern(optimizedCode, "public\\s+class\\s+(\\w+)");

		if (originalClass != null && !originalClass.equals(optimizedClass)) {
			result.addError("Rule violation: The public class name was modified. Expected: " + originalClass);
		} else if (optimizedClass == null) {
			result.addError("Structural error: Could not find a public class in the generated code.");
			return result;
		}

		if (result.isValid()) {
			try {
				compileCode(optimizedClass, optimizedCode, result);
			} catch (Exception e) {
				result.addError("Internal compilation error: " + e.getMessage());
			}
		}
		return result;

	}

	private String findPattern(String code, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(code);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private String cleanMarkdown(String aiResponse) {
		if (aiResponse == null)
			return "";
		int startIndex = aiResponse.indexOf("```");
		if (startIndex != -1) {
			int startOfCode = aiResponse.indexOf('\n', startIndex);
			if (startOfCode != -1) {
				int endIndex = aiResponse.indexOf("```", startOfCode);
				if (endIndex != -1) {
					return aiResponse.substring(startOfCode + 1, endIndex).trim();
				}
			}
		}
		return aiResponse.trim();
	}

	private void compileCode(String className, String code, ValidationResult result) throws IOException {
		Path tempDir = Files.createTempDirectory("ai_compiler_");
		File sourceFile = new File(tempDir.toFile(), className + ".java");
		Files.writeString(sourceFile.toPath(), code);

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			result.addError("Error: Java compiler not found. Please ensure you are using a JDK.");
			return;
		}

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

		Iterable<? extends JavaFileObject> compilationUnits = fileManager
				.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null,
				compilationUnits);

		boolean isSuccess = task.call();

		if (!isSuccess) {
			for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
				result.addError(String.format("Syntax error on line %d: %s", diagnostic.getLineNumber(),
						diagnostic.getMessage(null)));
			}
		}
		fileManager.close();
		Files.deleteIfExists(sourceFile.toPath());
		Files.deleteIfExists(new File(tempDir.toFile(), className + ".class").toPath());
		Files.deleteIfExists(tempDir);
	}
}
