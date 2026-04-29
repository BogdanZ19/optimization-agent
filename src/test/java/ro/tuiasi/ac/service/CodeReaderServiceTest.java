package ro.tuiasi.ac.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Importăm AssertJ, standardul în industrie pentru teste Spring Boot
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Teste pentru CodeReaderService")
class CodeReaderServiceTest {

    private CodeReaderService codeReaderService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        codeReaderService = new CodeReaderService();
    }

    @Test
    @DisplayName("Ar trebui să citească tot conținutul unui fișier valid")
    void shouldReadFileContentSuccessfully() throws IOException {
        // GIVEN
        Path fisierTest = tempDir.resolve("ValidCode.java");
        String continutAsteptat = "public class ValidCode {\n\t// Test\n}";
        Files.writeString(fisierTest, continutAsteptat);

        // WHEN 
        String continutCitit = codeReaderService.fileRead(fisierTest);

        // THEN 
        assertThat(continutCitit)
                .as("Textul citit trebuie să fie identic cu cel din fișier")
                .isEqualTo(continutAsteptat)
                .isNotBlank();
    }

    @Test
    @DisplayName("Ar trebui să returneze un text gol dacă fișierul nu conține nimic")
    void shouldReturnEmptyStringForEmptyFile() throws IOException {
        // GIVEN
        Path fisierGol = Files.createFile(tempDir.resolve("Empty.java"));

        // WHEN
        String continutCitit = codeReaderService.fileRead(fisierGol);

        // THEN
        assertThat(continutCitit)
                .as("Un fișier gol ar trebui să returneze un String gol")
                .isEmpty();
    }

    @Test
    @DisplayName("Ar trebui să citească corect caracterele speciale (UTF-8)")
    void shouldReadSpecialCharactersCorrectly() throws IOException {
        // GIVEN
        Path fisierDiacritice = tempDir.resolve("Diacritice.txt");
        String textCuDiacritice = "Cuvinte românești: ă, î, â, ș, ț. Cod: System.out.println(\"Salut!\");";
        Files.writeString(fisierDiacritice, textCuDiacritice);

        // WHEN
        String continutCitit = codeReaderService.fileRead(fisierDiacritice);

        // THEN
        assertThat(continutCitit).isEqualTo(textCuDiacritice);
    }

    @Test
    @DisplayName("Ar trebui să arunce IOException dacă fișierul nu există")
    void shouldThrowExceptionWhenFileNotFound() {
        // GIVEN
        Path fisierInexistent = tempDir.resolve("Fantoma.java");

        // WHEN & THEN
        assertThatThrownBy(() -> codeReaderService.fileRead(fisierInexistent))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Fantoma.java");
    }
}