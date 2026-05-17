package ro.tuiasi.ac.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

/**
 * Service responsible for creating backups of files.
 */
@Service
public class BackupService {

	/**
	 * Creates a backup copy of the specified file in the same directory.
	 * The backup file name is generated from the original file name,
	 * appended with a timestamp and the .bak extension.
	 *
	 * @param filePath the path to the file to back up
	 * @return the path to the newly created backup file
	 * @throws IOException if an I/O error occurs during the copy operation
	 */
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
