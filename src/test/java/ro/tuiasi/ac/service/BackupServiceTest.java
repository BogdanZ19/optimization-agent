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

@DisplayName("Teste pentru BackupService")
public class BackupServiceTest {

    private BackupService backupService;

    // Magia JUnit 5: creează un folder temporar care se șterge singur după test
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        backupService = new BackupService();
    }

    @Test
    @DisplayName("Ar trebui să creeze o copie exactă a fișierului, adăugând timestamp și extensia .bak")
    void shouldCreateBackupSuccessfully() throws IOException {
        // GIVEN (Pregătim terenul)
        // Creăm un fișier temporar și îi punem niște text în el
        Path fisierOriginal = tempDir.resolve("Main.java");
        String continutOriginal = "public class Main { System.out.println(\"Test\"); }";
        Files.writeString(fisierOriginal, continutOriginal);

        // WHEN (Executăm acțiunea)
        Path caleaBackup = backupService.createBackup(fisierOriginal);

        // THEN (Verificăm rezultatele)
        // 1. Verificăm dacă fișierul nou chiar există pe hard-disk
        assertThat(Files.exists(caleaBackup))
                .as("Fișierul de backup ar trebui să fie creat fizic")
                .isTrue();

        // 2. Verificăm dacă numele noului fișier respectă regula (ex: Main.java_20260506_101530.bak)
        String numeBackup = caleaBackup.getFileName().toString();
        assertThat(numeBackup)
                .as("Numele backup-ului trebuie să înceapă cu numele original și să se termine în .bak")
                .startsWith("Main.java_")
                .endsWith(".bak");

        // 3. Verificăm dacă textul copiat este absolut identic cu cel original
        String continutBackup = Files.readString(caleaBackup);
        assertThat(continutBackup)
                .as("Conținutul copiei de siguranță trebuie să fie identic cu originalul")
                .isEqualTo(continutOriginal);
    }

    @Test
    @DisplayName("Ar trebui să arunce o excepție (IOException) dacă fișierul original nu există deloc")
    void shouldThrowExceptionWhenFileDoesNotExist() {
        // GIVEN
        Path fisierInexistent = tempDir.resolve("CodFals.java");

        // WHEN & THEN
        // Verificăm dacă aplicația reacționează corect (dând eroare) în loc să se blocheze
        assertThatThrownBy(() -> backupService.createBackup(fisierInexistent))
                .isInstanceOf(IOException.class);
    }
}