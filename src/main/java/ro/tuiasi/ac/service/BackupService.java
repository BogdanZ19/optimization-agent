package ro.tuiasi.ac.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class BackupService {
	public Path createBackup(Path filePath) throws IOException {
		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String timestamp = LocalDateTime.now().format(date);

		String originalFileName = filePath.getFileName().toString();
		String backupFileName = originalFileName + "_" + timestamp + ".bak";

		Path backupFilePath = filePath.resolveSibling(backupFileName);

		Files.copy(filePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);
		return backupFilePath;
	}
}
