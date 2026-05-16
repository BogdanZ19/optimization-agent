package ro.tuiasi.ac.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Teste pentru FilePathInputService")
public class FilePathInputServiceTest {

	private FilePathInputService filePathInputService;

	// JUnit va crea și șterge automat acest folder temporar pentru fiecare test
	@TempDir
	Path tempDir;

	@BeforeEach
	void setUp() {
		filePathInputService = new FilePathInputService();
	}

	@Test
	@DisplayName("Ar trebui să returneze Path-ul atunci când fișierul este valid și este .java")
	void shouldReturnPathForValidJavaFile() throws IOException {
		// GIVEN: Creăm un fișier temporar valid .java
		Path validFile = tempDir.resolve("Main.java");
		Files.createFile(validFile);

		// WHEN: Apelăm serviciul
		Path result = filePathInputService.validateAndGetPath(validFile.toString());

		// THEN: Ne asigurăm că ne întoarce exact calea corectă
		assertThat(result).isEqualTo(validFile);
	}

	@Test
	@DisplayName("Ar trebui să arunce excepție dacă string-ul este null")
	void shouldThrowWhenPathIsNull() {
		assertThatThrownBy(() -> filePathInputService.validateAndGetPath(null))
				.isInstanceOf(IllegalArgumentException.class).hasMessageContaining("is empty or null");
	}

	@Test
	@DisplayName("Ar trebui să arunce excepție dacă string-ul este gol sau are doar spații")
	void shouldThrowWhenPathIsEmpty() {
		assertThatThrownBy(() -> filePathInputService.validateAndGetPath("   "))
				.isInstanceOf(IllegalArgumentException.class).hasMessageContaining("is empty or null");
	}

	@Test
	@DisplayName("Ar trebui să arunce excepție dacă fișierul nu există fizic pe disc")
	void shouldThrowWhenFileDoesNotExist() {
		String fakePath = tempDir.resolve("FisierCareNuExista.java").toString();

		assertThatThrownBy(() -> filePathInputService.validateAndGetPath(fakePath))
				.isInstanceOf(IllegalArgumentException.class).hasMessageContaining("does not exist");
	}

	@Test
	@DisplayName("Ar trebui să arunce excepție dacă a fost oferită calea către un folder (director)")
	void shouldThrowWhenPathIsDirectory() {
		// GIVEN: tempDir este un folder, nu un fișier
		String dirPath = tempDir.toString();

		assertThatThrownBy(() -> filePathInputService.validateAndGetPath(dirPath))
				.isInstanceOf(IllegalArgumentException.class).hasMessageContaining("is a directory");
	}

	@Test
	@DisplayName("Ar trebui să arunce excepție dacă fișierul nu are extensia .java")
	void shouldThrowWhenFileIsNotJava() throws IOException {
		// GIVEN: Creăm un fișier text în loc de java
		Path textFile = tempDir.resolve("document.txt");
		Files.createFile(textFile);

		assertThatThrownBy(() -> filePathInputService.validateAndGetPath(textFile.toString()))
				.isInstanceOf(IllegalArgumentException.class).hasMessageContaining("must be a Java source file");
	}
}