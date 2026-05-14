package ro.tuiasi.ac.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FolderScannerService {
	@Value("#{'${scanner.allowed-extensions}'.split(',')}")
	private List<String> allowedExtensions;
	@Value("#{'${scanner.ignored-folders}'.split(',')}")
	private List<String> ignoredFolders;
	@Value("${app.max-file-size}")
	private long bytesLimit;

	public List<Path> projectScan(Path startDir) throws IOException {
		try (Stream<Path> flux = Files.walk(startDir)) {
			return flux.filter(Files::isRegularFile).filter(this::isValidDir).collect(Collectors.toList());

		}
	}

	private boolean isValidDir(Path dirPath) {
		String path2String = dirPath.toString();
		String separator = FileSystems.getDefault().getSeparator();

		for (String ignoredFolder : ignoredFolders) {
			String ignoreddir = separator + ignoredFolder + separator;
			if (path2String.contains(ignoreddir)) {
				return false;
			}
		}
		String dirName = dirPath.getFileName().toString().toLowerCase();
		boolean hasAllowedExtension = allowedExtensions.stream().anyMatch(extension -> dirName.endsWith(extension));

		if (!hasAllowedExtension) {
			return false;
		}

		try {
			long bytesSize = Files.size(dirPath);
			return bytesSize <= bytesLimit;
		} catch (IOException e) {
			return false;
		}

	}

}