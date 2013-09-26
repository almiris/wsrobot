package fr.almiris.open.wsrobot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RobotRunner {
	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				ObjectMapper mapper = new ObjectMapper();
				RobotSuite suite = mapper.readValue(readFileStrippingComments(args[0]), RobotSuite.class);
				suite.run();
			}
		}
		catch (Exception e) {
			System.out.println("Un problème est survenu : " + e.toString());
		}
	}
	public static String readFileStrippingComments(String file) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			String line;
			int index = 1;
			while ((line = reader.readLine()) != null) {
				if (line.trim().startsWith("//") == false) {
					System.out.println(pad(index, 10) + " : " + line);
					index++;
					pw.println(line);
				}			
			}
			String str = sw.toString();
			return str == null ? str : str.replace("\t", "").replace("\r", "").replace("\n", "");
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public static String pad(int value, int len) {
		String str = String.valueOf(value);
		StringWriter sw = new StringWriter();
		sw.append(str);
		for (int i = str.length(); i < len; i++) {
			sw.append(" ");
		}
		return sw.toString();
	}
}
