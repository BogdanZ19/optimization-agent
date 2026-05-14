package ro.tuiasi.ac.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CodeReaderService {
	public String fileRead(Path filePath) throws IOException {
		return Files.readString(filePath);
	}

}
