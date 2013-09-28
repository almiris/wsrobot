package fr.almiris.open.wsrobot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.almiris.open.wsrobot.conf.RobotSuiteConf;
import fr.almiris.open.wsrobot.report.RobotSuiteReport;
import fr.almiris.open.wsrobot.util.FileTool;
import fr.almiris.open.wsrobot.util.TemplateTool;

/**
 * Usage : RobotRunner <configuration file> <json output> <html output>
 * @author yherrmann
 *
 */
public class RobotRunner {
	
	public static void main(String[] args) {
		RobotRunner robot = new RobotRunner();
		FileReader fr = null;
		FileWriter fwj = null;
		FileWriter fwh = null;
		try {
			if (args.length > 0) {
				fr = new FileReader(args[0]);
				RobotSuiteConf conf = robot.readConf(fr);
				RobotSuite suite = new RobotSuite();
				RobotSuiteReport report = suite.run(conf);
				if (args.length > 1) {
					fwj = new FileWriter(args[1]);
					robot.writeReportAsJson(report, fwj);
				}
				if (args.length > 2) {
					fwh = new FileWriter(args[2]);
					robot.writeReportAsHTML(report, fwh);
				}
				System.exit(report.getErrorCount());
			}
			System.exit(0);
		}
		catch (Exception e) {
			System.out.println("An error has occurred : " + e.toString());			
		}
		finally {
			try {
				if (fr != null) {
					fr.close();
				}
			}
			catch (IOException ioe) {				
				System.out.println("An error has occurred : " + ioe.toString());			
			}
			try {
				if (fwj != null) {
					fwj.close();
				}
			}
			catch (IOException ioe) {				
				System.out.println("An error has occurred : " + ioe.toString());			
			}
			try {
				if (fwh != null) {
					fwh.close();
				}
			}
			catch (IOException ioe) {				
				System.out.println("An error has occurred : " + ioe.toString());			
			}
		}
	}
	
	public RobotSuiteConf readConf(Reader reader) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		return getConfFromString(FileTool.readFileStrippingComments(reader));
	}

	public RobotSuiteConf getConfFromString(String configuration) throws JsonMappingException, JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(configuration, RobotSuiteConf.class);
	}

	public void writeReportAsJson(RobotSuiteReport report, Writer writer) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.writeValue(writer, report);		
	}

	public void writeReportAsHTML(RobotSuiteReport report, Writer writer) throws TemplateTool.ProcessingException {
		(new TemplateTool()).process("default.mustache", report, writer);
	}
}
