package fr.almiris.open.wsrobot.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

public class ReaderTool {

	/**
	 * The method does not close the reader.
	 * @param reader
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static String readFileStrippingComments(Reader reader) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		BufferedReader br = new BufferedReader(reader);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String line;
		int index = 1;
		while ((line = br.readLine()) != null) {
			if (line.trim().startsWith("//") == false) {
				index++;
				pw.println(line);
			}			
		}
		String str = sw.toString();
		return str == null ? str : str.replace("\t", "").replace("\r", "").replace("\n", "");
	}

}
