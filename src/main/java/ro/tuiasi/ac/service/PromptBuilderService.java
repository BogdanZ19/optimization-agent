package ro.tuiasi.ac.service;

import java.nio.file.Path;

import org.springframework.stereotype.Service;
@Service
public class PromptBuilderService {
		public String FirstOptimizationPrompt(Path relativePath, String sourceCode) {
			String prompt= new String("You are a Java code optimization agent. You receive exactly one Java file, not a folder or full "
					+"project. Improve performance and readability while preserving behavior. Rules: 1. Preserve "
					+"package declaration. 2. Do not rename public classes, public methods, constructors, or public "
					+"fields. 3. Return the full optimized Java file, not a partial patch. 4. Do not invent "
					+"dependencies unless they are from the Java standard library. 5. Keep the code beginner-friendly. 6. Don't add any comments to explain the changes "
					+"Return the full optimized Java code inside one markdown "
					+"code block. Java file path: "+ relativePath + " Original code: ```"+ sourceCode+" ```");
			return prompt;
		}
		public String LoopOptimizationPrompt(Path relativePath, String sourceCode, String optimizedCode ,String validationErrors)
		{
			String prompt=new String("The code you generated did not pass validation. Fix the optimized Java code according to these validation errors \n  "
					+validationErrors+"  \n Original file path  "+ relativePath+"  \n  Original code :\n "+ sourceCode +" \n Your previous optimized code : \n"
					+ optimizedCode+" \n Return only the completed corect java code inside one markdown block");
			return prompt;
		}
}
