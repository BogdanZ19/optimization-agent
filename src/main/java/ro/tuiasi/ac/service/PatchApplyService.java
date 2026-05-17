package ro.tuiasi.ac.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

/**
 * Service responsible for applying patches or updates to files.
 * <p>
 * This Spring-managed service provides a straightforward mechanism to write
 * or overwrite file content with newly provided data.
 * </p>
 */
@Service
public class PatchApplyService {

	/**
	 * Applies a patch by writing the specified content to the given file path.
	 * <p>
	 * This method utilizes {@link Files#writeString} to write the text. If the file 
	 * already exists, its contents will be completely overwritten. If the file does 
	 * not exist, a new file will be created.
	 * </p>
	 *
	 * @param filePath   the {@link Path} to the target file where the content should be written
	 * @param newContent the new string content to write to the file
	 * @throws IOException if an I/O error occurs while opening, creating, or writing to the file
	 */
	public void applyPatch(Path filePath, String newContent) throws IOException {
		Files.writeString(filePath, newContent);
	}
}
