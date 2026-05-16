package ro.tuiasi.ac.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class FilePathInputService {
	public Path validateAndGetPath(String filePathString) {
		if (filePathString == null || filePathString.trim().isEmpty()) {
			throw new IllegalArgumentException("Error: The provided file path is empty or null.");
		}
		Path path = Paths.get(filePathString.trim());
		if (!Files.exists(path)) {
			throw new IllegalArgumentException("Error: The file does not exist at the provided path -> " + filePathString);
		}
		if (!Files.isRegularFile(path)) {
			throw new IllegalArgumentException("Error: The provided path is a directory -> " + filePathString);
		}
		if (!Files.isReadable(path)) {
			throw new IllegalArgumentException("Error: The file exists but cannot be read -> " + filePathString);
		}
		if (!path.toString().endsWith(".java")) {
			throw new IllegalArgumentException("Error: The file must be a Java source file -> " + filePathString);
		}

		return path;
	}
}
