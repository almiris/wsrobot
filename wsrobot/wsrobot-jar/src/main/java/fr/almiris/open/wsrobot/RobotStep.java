package fr.almiris.open.wsrobot;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

import fr.almiris.open.wsrobot.conf.RobotSQLConf;
import fr.almiris.open.wsrobot.conf.RobotScenarioConf;
import fr.almiris.open.wsrobot.conf.RobotServiceConf;
import fr.almiris.open.wsrobot.conf.RobotStepConf;
import fr.almiris.open.wsrobot.conf.RobotSuiteConf;
import fr.almiris.open.wsrobot.http.RobotHttpClient;
import fr.almiris.open.wsrobot.report.RobotStepReport;

public class RobotStep {

	public enum Status {
		OK(0,"ok"),
		ERROR_SERVICE_NOT_FOUND(1, "service not found"),
		ERROR_BAD_RETURN_STATUS(2, "bad return status"),
		ERROR_CONTROL_NULL_CONTENT(3, "control : content is null"),
		ERROR_CONTROL_FAILED(4, "control has failed"),
		ERROR_RESULT_NOT_FOUND(5, "result not found");

		private final int code;
		private final String message;

		Status(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}
	}
	
	public RobotStep() {
	}
	
	public RobotStepReport run(RobotSuiteConf suiteConf, RobotScenarioConf scenarioConf, RobotStepConf stepConf, RobotSuite suite) throws UnsupportedEncodingException, SQLException {
		
		RobotStepReport report = new RobotStepReport();
		
		report.setConf(stepConf);
		
		if (stepConf.getService() != null) {
			if (execURL(suiteConf, stepConf, suite, report) == false) {
				return report;
			}			
		}
		else if (stepConf.getSql() != null) {
			if (execSQL(suiteConf, stepConf, suite, report) == false) {
				return report;
			}						
		}
				
		if (stepConf.getPresults() != null) {
			for (String property : stepConf.getPresults().keySet()) {
				String value = suiteConf.replaceProperties(stepConf.getPresults().get(property));
				suiteConf.getProperties().put(property, value);
				suite.getLogger().debug("Property " + property + " has been set to " + value);
				report.setProperty(property, value);
			}
		}

		if (stepConf.getPcontrols() != null) {
			for (String control : stepConf.getPcontrols().keySet()) {
				String actualValue = suiteConf.replaceProperties(control);
				String expectedValue = suiteConf.replaceProperties(stepConf.getPcontrols().get(control));
				if ((expectedValue.startsWith("rxp:") && Pattern.matches(expectedValue.substring("rxp:".length()), actualValue) == false)||(expectedValue.equals(actualValue) == false)) {
					report.setStatus(Status.ERROR_CONTROL_FAILED);
					report.setFailedControl(control);
					report.setFailedControlExpectedValue(expectedValue);
					report.setFailedControlActualValue(actualValue);
					suite.getLogger().error("Control failed : " + control + " (expected = " + expectedValue + "; actual = " + actualValue + ")");
					return report;
				}
			}
		}

		return report;
	}

	private boolean execURL(RobotSuiteConf suiteConf, RobotStepConf stepConf, RobotSuite suite, RobotStepReport report) throws UnsupportedEncodingException {
		RobotServiceConf serviceConf = suiteConf.getServices().get(stepConf.getService());
		
		if (serviceConf == null) {
			report.setStatus(Status.ERROR_SERVICE_NOT_FOUND);
			suite.getLogger().error("Service not found");
			return false;
		}
		
		report.setService(serviceConf);

		String fullURL = MessageFormat.format(suiteConf.replaceProperties(serviceConf.getUrl()), encodeParams(suiteConf.replaceProperties(stepConf.getParams()))).toString();
		suite.getLogger().debug("-----");
		suite.getLogger().debug("Executing service : " + serviceConf.getName() + " ("+ fullURL + ")");
		
		report.setFullURL(fullURL);
				
		Map<String,String> requestHeaders = new HashMap<String,String>();
		
		if (serviceConf.getHeaders() != null) {
			for (String header : serviceConf.getHeaders().keySet()) {
				String value = suiteConf.replaceProperties(serviceConf.getHeaders().get(header));
				if (value != null) {
					requestHeaders.put(header, value);
					suite.getLogger().debug("using header " + header + " = " + value);
				}
			}
		}
		
		if (stepConf.getHeaders() != null) {
			for (String header : stepConf.getHeaders().keySet()) {
				String value = suiteConf.replaceProperties(stepConf.getHeaders().get(header));
				if (value != null) {
					requestHeaders.put(header, value);
					suite.getLogger().debug("using header " + header + " = " + value);
				}
			}			
		}
		
		report.setHeaders(requestHeaders);
		
		RobotHttpClient rp = new RobotHttpClient();
		
		long start = System.currentTimeMillis();
		
		if ("get".equalsIgnoreCase(serviceConf.getMethod()) == true) {
			rp.get(fullURL, requestHeaders);
		}
		else if ("post".equalsIgnoreCase(serviceConf.getMethod()) == true) {
			String data = suiteConf.replaceProperties(stepConf.getData());
			report.setData(data);
			rp.post(fullURL, requestHeaders, data);
		}
		else if ("delete".equalsIgnoreCase(serviceConf.getMethod()) == true) {
			rp.delete(fullURL, requestHeaders);
		}
		
		int status = rp.getResponse().getStatusLine().getStatusCode();
		String content = rp.getContent();
		boolean stepOk = status == stepConf.getStatus();
		
		report.setExecutionTime(System.currentTimeMillis() - start);
		report.setActualStatus(status);
		report.setActualContent(content);		
		report.setStatus(stepOk ? Status.OK : Status.ERROR_BAD_RETURN_STATUS);
		
		if (stepOk == false) {
			suite.getLogger().error("Status code is incorrect : " + status + " (" + stepConf.getStatus() + " was expected)");
			return false;
		}

		suite.getLogger().debug("Status code is as expected : " + status);
		if (content != null) {
			suite.getLogger().debug("Content : " + content);
		}
		
		if (stepConf.getJcontrols() != null) {
			if (content == null) {
				suite.getLogger().error("Content is null but controls are defined");
				report.setStatus(Status.ERROR_CONTROL_NULL_CONTENT);
				return false;
			}

			for (String jsonPath : stepConf.getJcontrols().keySet()) {
				String expectedValue = suiteConf.replaceProperties(stepConf.getJcontrols().get(jsonPath));
				String actualValue = JsonPath.read(content, jsonPath).toString();
				if ((expectedValue.startsWith("rxp:") == true && Pattern.matches(expectedValue.substring("rxp:".length()), actualValue) == false)
						|| (expectedValue.startsWith("rxp:") == false && expectedValue.equals(actualValue) == false)) {
					stepOk = false;
					report.setStatus(Status.ERROR_CONTROL_FAILED);
					report.setFailedControl(jsonPath);
					report.setFailedControlExpectedValue(expectedValue);
					report.setFailedControlActualValue(actualValue);
					suite.getLogger().error("Control failed : " + jsonPath + " (expected = " + expectedValue + "; actual = " + actualValue + ")");
					return false;
				}
			}
		}
		
		if (serviceConf.getJresults() != null && status >= 200 && status < 300) {
			for (String property : serviceConf.getJresults().keySet()) {
				String jsonPath = serviceConf.getJresults().get(property);
				String value = null;
				try {
					value = JsonPath.read(content, jsonPath);
				}
				catch (InvalidPathException ipa) {					
				}
				finally {
					if (value == null) {
						stepOk = false;
						report.setStatus(Status.ERROR_RESULT_NOT_FOUND);
						report.setResultNotFound(jsonPath);
						suite.getLogger().error("Result not found : " + jsonPath);
						return false;
					}
					else {
						suiteConf.getProperties().put(property, value);
						suite.getLogger().debug("Property " + property + " has been set to " + value);
						report.setProperty(property, value);
					}					
				}
			}			
		}
		
		return true;
	}
	
	private boolean execSQL(RobotSuiteConf suiteConf, RobotStepConf stepConf, RobotSuite suite, RobotStepReport report) throws UnsupportedEncodingException, SQLException {
		RobotSQLConf conf = stepConf.getSql();
		
		suite.getLogger().debug("-----");
		suite.getLogger().debug("sql:");
		
		boolean result = true;
		Connection conn = null;
		long start = System.currentTimeMillis();
		
		try {
			if (conf.getUser() == null) {
				conn = DriverManager.getConnection(suiteConf.replaceProperties(conf.getUrl()));
			}
			else {
				conn = DriverManager.getConnection(suiteConf.replaceProperties(conf.getUrl()), suiteConf.replaceProperties(conf.getUser()), suiteConf.replaceProperties(conf.getPassword()));			
			}
			
			List<String> reqs = conf.getReqs();
			
			start = System.currentTimeMillis();

			for (String req : reqs) {
				conn.setAutoCommit(false);
				String request = suiteConf.replaceProperties(req);
				suite.getLogger().debug("Executing SQL request : " + request);
				PreparedStatement stmt = conn.prepareStatement(request);
				if (request.length() >= "select".length() && request.substring(0, "select".length()).equalsIgnoreCase("select")) {
					ResultSet rs = stmt.executeQuery();
					if (rs != null) {
						int index = 0;
						ResultSetMetaData metaData = rs.getMetaData();
						int colCount = metaData.getColumnCount();
						result = colCount > 0 ? true : false;
						
						// reset properties that will be extracted from the result set
						if (colCount > 0) {
							for (int col = 0; col < colCount; col++) {
								String prefix = metaData.getColumnLabel(col + 1) + ".";
								Map<String,String> properties = suiteConf.getProperties();
								Set<String> keySet = properties.keySet();
								Iterator<String> keyIterator = keySet.iterator();
								List<String> keysToRemove = new ArrayList<String>();
								while (keyIterator.hasNext()) {
									String key = keyIterator.next();
									if (key.startsWith(prefix)) {
										keysToRemove.add(key);
									}
								}
								for (String key : keysToRemove) {
									properties.remove(key);
								}
							}							
						}
						
						// set properties from the result set
						while (rs.next()) {
							for (int col = 0; col < colCount; col++) {
								String property = metaData.getColumnLabel(col + 1) + "." + index;
								String value = rs.getString(col + 1);
								suiteConf.getProperties().put(property, value);
								suite.getLogger().debug("Property " + property + " has been set to " + value);
								report.setProperty(property, value);								
							}
							index++;
						};
					}
					else { 
						result = false;
					}
					rs.close();
				}
				else {
					result = stmt.execute();
				}
				stmt.close();
				conn.commit();
			}
			
			report.setExecutionTime(System.currentTimeMillis() - start);
		}
		catch (SQLException e) {
			report.setExecutionTime(System.currentTimeMillis() - start);
			if (conn != null) {
				conn.rollback();
			}
			throw e;
		}
		finally {
			if (conn != null) {
				conn.close();
			}
		}
		
		report.setStatus(RobotStep.Status.OK);
		return result;
	}
	
	private String[] encodeParams(String[] strArr) throws UnsupportedEncodingException {
		if (strArr == null) {
			return null;
		}
		
		String[] result = new String[strArr.length];
		
		for (int i = 0; i < strArr.length; i++) {
			result[i] = URLEncoder.encode(strArr[i], "utf-8");
		}
		
		return result;
	}

}
