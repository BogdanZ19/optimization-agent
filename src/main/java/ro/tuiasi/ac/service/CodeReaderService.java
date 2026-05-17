package ro.tuiasi.ac.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service responsible for reading the contents of source files.
 * <p>
 * This Spring-managed service provides a lightweight utility to load the entire 
 * text content of a given file into memory as a standard Java string.
 * </p>
 */
@Service
public class CodeReaderService {

	/**
     * Reads the entire content of a specified file into a string.
     * <p>
     * This method utilizes {@link Files#readString(Path)} to extract all characters 
     * from the file at the provided path. It is typically used to load source code 
     * files for further processing or optimization.
     * </p>
     *
     * @param filePath the {@link Path} locating the file to be read
     * @return a {@link String} containing the complete textual contents of the file
     * @throws IOException if an I/O error occurs while reading the file, such as 
     *                     if the file does not exist or access is denied
     */
	public String fileRead(Path filePath) throws IOException {
		return Files.readString(filePath);
	}

}
