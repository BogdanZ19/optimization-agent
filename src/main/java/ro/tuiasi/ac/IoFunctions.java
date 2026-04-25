package ro.tuiasi.ac;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IoFunctions {
	
	static void read() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("fisier.txt"));
		String linie;
		linie = br.readLine();
		while(linie != null)
		{
			linie = br.readLine();
		}
	}

}
