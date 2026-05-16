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

@DisplayName("Teste pentru PatchApplyService")
public class PatchApplyServiceTest {

	private PatchApplyService patchApplyService;

	@TempDir
	Path tempDir;

	@BeforeEach
	void setUp() {
		patchApplyService = new PatchApplyService();
	}

	@Test
	@DisplayName("Ar trebui să suprascrie cu succes fișierul original cu noul cod")
	void shouldApplyPatchSuccessfully() throws IOException {
		// GIVEN (Avem un fișier cu cod vechi și pregătim codul nou)
		Path fisierDeModificat = tempDir.resolve("Main.java");
		Files.writeString(fisierDeModificat, "Cod Vechi, Neoptimizat");

		String codNouDeLaGemini = "Cod Nou, Curat si Optimizat";

		// WHEN (Apelăm serviciul tău ca să aplice noul cod)
		patchApplyService.applyPatch(fisierDeModificat, codNouDeLaGemini);

		// THEN (Verificăm dacă textul s-a schimbat)
		String continutDupaPatch = Files.readString(fisierDeModificat);
		assertThat(continutDupaPatch).as("Conținutul fișierului trebuie să fie exact codul nou")
				.isEqualTo(codNouDeLaGemini);
	}

	@Test
	@DisplayName("Ar trebui să arunce IOException dacă nu poate scrie (ex: calea e un folder, nu un fișier)")
	void shouldThrowExceptionWhenCannotWrite() {
		// GIVEN (Încercăm să scriem direct peste folderul temporar, ceea ce sistemul de
		// operare nu permite)
		String codNou = "Test Eroare";

		// WHEN & THEN (Trebuie să dea eroare)
		assertThatThrownBy(() -> patchApplyService.applyPatch(tempDir, codNou)).isInstanceOf(IOException.class);
	}
}