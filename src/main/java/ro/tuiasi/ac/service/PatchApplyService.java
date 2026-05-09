package ro.tuiasi.ac.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

@Service
public class PatchApplyService {
	
	public static void applyPatch(Path filePath, String newContent)throws IOException{
		Files.writeString(filePath, newContent);
	}
}
