package fr.almiris.open.wsrobot;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

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
	
	public RobotStepReport run(RobotSuiteConf suiteConf, RobotScenarioConf scenarioConf, RobotStepConf stepConf, RobotSuite suite) {
		
		RobotStepReport report = new RobotStepReport();
		
		report.setConf(stepConf);
		
		RobotServiceConf serviceConf = suiteConf.getServices().get(stepConf.getService());
		
		if (serviceConf == null) {
			report.setStatus(Status.ERROR_SERVICE_NOT_FOUND);
			return report;
		}
		
		report.setService(serviceConf);
		
		String fullURL = MessageFormat.format(suiteConf.replaceProperties(serviceConf.getUrl()), suiteConf.replaceProperties(stepConf.getParams())).toString();
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
			return report;
		}

		suite.getLogger().debug("Status code is as expected : " + status);
		if (content != null) {
			suite.getLogger().debug("Content : " + content);
		}
		
		if (stepConf.getJcontrols() != null) {
			if (content == null) {
				suite.getLogger().error("Content is null but controls are defined");
				report.setStatus(Status.ERROR_CONTROL_NULL_CONTENT);
				return report;
			}

			for (String jsonPath : stepConf.getJcontrols().keySet()) {
				String expectedValue = suiteConf.replaceProperties(stepConf.getJcontrols().get(jsonPath));
				String actualValue = JsonPath.read(content, jsonPath).toString();
				if ((expectedValue.startsWith("rxp:") && Pattern.matches(expectedValue.substring("rxp:".length()), actualValue) == false)||(expectedValue.equals(actualValue) == false)) {
					stepOk = false;
					report.setStatus(Status.ERROR_CONTROL_FAILED);
					report.setFailedControl(jsonPath);
					report.setFailedControlExpectedValue(expectedValue);
					report.setFailedControlActualValue(actualValue);
					suite.getLogger().error("Control failed : " + jsonPath + " (expected = " + expectedValue + "; actual = " + actualValue + ")");
					return report;
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
						return report;
					}
					else {
						suiteConf.getProperties().put(property, value);
						suite.getLogger().debug("Property " + property + " has been set to " + value);
						report.setProperty(property, value);
					}					
				}
			}
		}
		
		return report;
	}
}
