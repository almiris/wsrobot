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
			ObjectMapper mapper = new ObjectMapper();
			RobotSuite suite = mapper.readValue(readFileStrippingComments(args[0]), RobotSuite.class);
			suite.run();
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
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("//") == false) {
					System.out.println(line);
					pw.println(line);
				}			
			}
			return sw.toString();
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
