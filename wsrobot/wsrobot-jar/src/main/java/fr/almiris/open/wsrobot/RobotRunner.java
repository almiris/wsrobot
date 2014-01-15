package fr.almiris.open.wsrobot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.almiris.open.wsrobot.conf.RobotSuiteConf;
import fr.almiris.open.wsrobot.report.RobotSuiteReport;
import fr.almiris.open.wsrobot.util.ReaderTool;
import fr.almiris.open.wsrobot.util.TemplateTool;

/**
 * Usage : RobotRunner <configuration file> <json output> <html output>
 * @author yherrmann
 *
 */
public class RobotRunner {
	
	public static void main(String[] args) {
		RobotRunner robot = new RobotRunner();
		OutputStreamWriter oswj = null;
		OutputStreamWriter oswh = null;
		try {
			if (args.length > 0) {
				RobotSuiteConf conf = robot.readConf(new File(args[0]));				
				robot.writeAsJson(conf, new OutputStreamWriter(System.out, Charset.forName("utf-8")));
				RobotSuite suite = new RobotSuite();
				RobotSuiteReport report = suite.run(conf);
				if (args.length > 1) {
					oswj = new OutputStreamWriter(new FileOutputStream(args[1]), Charset.forName("utf-8"));
					robot.writeAsJson(report, oswj);
				}
				if (args.length > 2) {
					oswh = new OutputStreamWriter(new FileOutputStream(args[2]), Charset.forName("utf-8"));
					robot.writeReportAsHTML(report, oswh);
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
				if (oswj != null) {
					oswj.close();
				}
			}
			catch (IOException ioe) {				
				System.out.println("An error has occurred : " + ioe.toString());			
			}
			try {
				if (oswh != null) {
					oswh.close();
				}
			}
			catch (IOException ioe) {				
				System.out.println("An error has occurred : " + ioe.toString());			
			}
		}
	}
	
	public RobotSuiteConf readConf(File file) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		InputStreamReader reader = null;
		try {
			RobotSuiteConf resultConf = new RobotSuiteConf();
			reader = new InputStreamReader(new FileInputStream(file), Charset.forName("utf-8"));				
			RobotSuiteConf conf = readConf(reader);
			if (conf.getImports() != null) {
				for (String importFilename : conf.getImports()) {
					File importFile = new File(file.getParent(), importFilename);
					RobotSuiteConf importConf = readConf(importFile);
					resultConf.mergeAndOverrideWith(importConf);
				}
			}
			resultConf.mergeAndOverrideWith(conf);
			return resultConf;
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public RobotSuiteConf readConf(Reader reader) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		return getConfFromString(ReaderTool.readFileStrippingComments(reader));
	}

	public RobotSuiteConf getConfFromString(String configuration) throws JsonMappingException, JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(configuration, RobotSuiteConf.class);
	}

	public void writeAsJson(Object obj, Writer writer) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		mapper.writeValue(writer, obj);		
	}

	public void writeReportAsHTML(RobotSuiteReport report, Writer writer) throws TemplateTool.ProcessingException {
		(new TemplateTool()).process("default.html", report, writer);
	}	
}
