package ro.tuiasi.ac.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for DiffService")
class DiffServiceTest {

	private DiffService diffService;

	@BeforeEach
	void setUp() {
		diffService = new DiffService();
	}

	@Test
	@DisplayName("Should detect when the AI adds lines of code")
	void shouldDetectAddedLines() {
		String original = "int x = 5;";
		String optimized = "int x = 5;\nSystem.out.println(x);";

		String result = diffService.generateSimpleDiff(original, optimized);

		assertThat(result).contains("-Original code lines: 1").contains("-Optimized code lines: 2")
				.contains("=> The AI added 1 lines in total.");
	}

	@Test
	@DisplayName("Should detect when the AI removes lines of code")
	void shouldDetectRemovedLines() {
		String original = "int x = 5;\n// a comment\n// another comment";
		String optimized = "int x = 5;";

		String result = diffService.generateSimpleDiff(original, optimized);

		assertThat(result).contains("-Original code lines: 3").contains("-Optimized code lines: 1")
				.contains("=> The AI removed 2 lines in total.");
	}

	@Test
	@DisplayName("Should detect when the code is modified but has the same line count")
	void shouldDetectModificationsWithSameLineCount() {
		String original = "int x = 5;\nint y = 10;";
		String optimized = "int x = 10;\nint y = 5;";

		String result = diffService.generateSimpleDiff(original, optimized);

		assertThat(result).contains("=> The number of lines is the same, but the content was modified.");
	}

	@Test
	@DisplayName("Should detect when the code is not touched at all")
	void shouldDetectIdenticalCode() {
		String code = "public class Main {\n}";

		String result = diffService.generateSimpleDiff(code, code);

		assertThat(result).contains("=> No modifications detected.");
	}

	@Test
	@DisplayName("Should handle null inputs perfectly without crashing")
	void shouldHandleNullInputs() {
		String result = diffService.generateSimpleDiff(null, null);

		assertThat(result).contains("-Original code lines: 0").contains("-Optimized code lines: 0")
				.contains("=> No modifications detected.");
	}
}