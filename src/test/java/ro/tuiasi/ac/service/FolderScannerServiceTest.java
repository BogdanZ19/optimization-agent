package ro.tuiasi.ac.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FolderScannerServiceTest {

    private FolderScannerService folderScannerService;

    @TempDir
    Path tempDir; // Creează un director temporar automat pentru fiecare test

    @BeforeEach
    void setUp() {
        folderScannerService = new FolderScannerService();
        
        // Injectăm manual valorile care ar veni din application.properties prin @Value
        ReflectionTestUtils.setField(folderScannerService, "allowedExtensions", Arrays.asList(".java", ".txt"));
        ReflectionTestUtils.setField(folderScannerService, "ignoredFolders", Arrays.asList("target", ".git"));
        ReflectionTestUtils.setField(folderScannerService, "bytesLimit", 1024L); // 1KB limită
    }

    @Test
    void testProjectScan_ShouldIncludeValidFiles() throws IOException {
        // GIVEN: Un fișier valid
        Path validFile = tempDir.resolve("Main.java");
        Files.write(validFile, "public class Main {}".getBytes());

        // WHEN
        List<Path> result = folderScannerService.projectScan(tempDir);

        // THEN
        assertEquals(1, result.size());
        assertTrue(result.contains(validFile));
    }

    @Test
    void testProjectScan_ShouldIgnoreFolders() throws IOException {
        // GIVEN: Un folder care trebuie ignorat (ex: target)
        Path targetDir = tempDir.resolve("target");
        Files.createDirectory(targetDir);
        Path ignoredFile = targetDir.resolve("Build.java");
        Files.write(ignoredFile, "some data".getBytes());

        // WHEN
        List<Path> result = folderScannerService.projectScan(tempDir);

        // THEN
        assertTrue(result.isEmpty(), "Fișierele din folderele ignorate nu ar trebui să apară");
    }

    @Test
    void testProjectScan_ShouldFilterByExtension() throws IOException {
        // GIVEN: Un fișier cu extensie nepermisă
        Path invalidFile = tempDir.resolve("image.png");
        Files.write(invalidFile, new byte[10]);

        // WHEN
        List<Path> result = folderScannerService.projectScan(tempDir);

        // THEN
        assertFalse(result.contains(invalidFile));
    }

    @Test
    void testProjectScan_ShouldFilterBySize() throws IOException {
        // GIVEN: Un fișier care depășește limita de 1024 bytes
        Path largeFile = tempDir.resolve("LargeFile.java");
        Files.write(largeFile, new byte[2048]);

        // WHEN
        List<Path> result = folderScannerService.projectScan(tempDir);

        // THEN
        assertTrue(result.isEmpty(), "Fișierele prea mari ar trebui ignorate");
    }
}