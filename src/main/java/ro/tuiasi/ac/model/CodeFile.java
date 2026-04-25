package ro.tuiasi.ac.model;
import java.nio.file.Path;
public record CodeFile(String fileName,
		Path fullPath,
		String relativePath,
		long sizeInKb) {}
