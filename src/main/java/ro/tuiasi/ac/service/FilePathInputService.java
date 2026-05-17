package ro.tuiasi.ac.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

/**
 * Service responsible for validating a file path string and returning the corresponding
 * {@link Path} instance when the specified file exists, is readable, and is a Java source file.
 */
@Service
public class FilePathInputService {
	/**
	 * Validates the provided file path string and returns a {@link Path} instance if the
	 * referenced file exists, is a regular file, is readable, and has a .java extension.
	 *
	 * @param filePathString the file path string to validate
	 * @return the validated {@link Path} for the Java source file
	 * @throws IllegalArgumentException if the path is null, empty, does not exist, is not a regular file,
	 *         is not readable, or does not point to a .java file
	 */
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
