package ro.tuiasi.ac.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ro.tuiasi.ac.model.ValidationResult;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Teste pentru CodeValidationService")
class CodeValidationServiceTest {

	private CodeValidationService validationService;

	@TempDir
	Path tempDir;

	private Path dummyFilePath;

	@BeforeEach
	void setUp() {
		validationService = new CodeValidationService();
		dummyFilePath = tempDir.resolve("Main.java");
	}

	@Test
	@DisplayName("Ar trebui să dea eroare dacă AI-ul returnează un cod gol")
	void shouldFailWhenOptimizedCodeIsEmpty() {
		ValidationResult result = validationService.validate("public class Main {}", "   ", dummyFilePath);

		assertThat(result.isValid()).isFalse();
		assertThat(result.getErrors().get(0)).contains("empty or missing");
	}

	@Test
	@DisplayName("Ar trebui să dea eroare dacă AI-ul modifică pachetul")
	void shouldFailWhenPackageIsChanged() {
		String originalCode = "package ro.tuiasi;\npublic class Main {}";
		String optimizedCode = "package ro.alta.companie;\npublic class Main {}";

		ValidationResult result = validationService.validate(originalCode, optimizedCode, dummyFilePath);

		assertThat(result.isValid()).isFalse();
		assertThat(result.getErrors().get(0)).contains("package declaration was modified");
	}

	@Test
	@DisplayName("Ar trebui să dea eroare dacă AI-ul redenumește clasa publică")
	void shouldFailWhenClassNameIsChanged() {
		String originalCode = "package ro.tuiasi;\npublic class Main {}";
		String optimizedCode = "package ro.tuiasi;\npublic class SuperMain {}";

		ValidationResult result = validationService.validate(originalCode, optimizedCode, dummyFilePath);

		assertThat(result.isValid()).isFalse();
		assertThat(result.getErrors().get(0)).contains("public class name was modified");
	}

	@Test
	@DisplayName("Ar trebui să prindă erorile de sintaxă (ex: lipsă punct și virgulă)")
	void shouldCatchSyntaxErrors() {
		String originalCode = "package ro.tuiasi;\npublic class Main { public void test() {} }";
		// Codul nou are o eroare: îi lipsește ; la finalul print-ului
		String optimizedCodeWithSyntaxError = "package ro.tuiasi;\npublic class Main { public void test() { System.out.println(\"Eroare\") } }";

		ValidationResult result = validationService.validate(originalCode, optimizedCodeWithSyntaxError, dummyFilePath);

		assertThat(result.isValid()).isFalse();
		// Compilatorul nativ va zice că așteaptă un ';'
		assertThat(result.getErrors().get(0)).contains("';' expected");
	}

	@Test
	@DisplayName("Ar trebui să valideze cu succes codul corect, chiar și curățând Markdown-ul")
	void shouldPassValidationForCorrectCodeWithMarkdown() {
		String originalCode = "package ro.tuiasi;\npublic class Main { public void test() {} }";

		// AI-ul returnează codul perfect, dar încadrat de Markdown
		String aiResponse = "```java\n" + "package ro.tuiasi;\n" + "public class Main {\n"
				+ "    public void test() {\n" + "        int x = 5;\n" + "    }\n" + "}\n" + "```";

		ValidationResult result = validationService.validate(originalCode, aiResponse, dummyFilePath);

		assertThat(result.isValid()).as("Codul ar trebui să fie valid după ce a fost curățat și compilat").isTrue();
		assertThat(result.getErrors()).isEmpty();
	}
}