package fr.almiris.open.wsrobot;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.jayway.jsonpath.JsonPath;

public class RobotStep {
	private String service;
	private Map<String,String> headers;
	private String[] params;
	private String data;
	private int status;
	private Map<String,String> controls;
	
	public RobotStep() {
	}
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String[] getParams() {
		return params;
	}
	
	public void setParams(String[] params) {
		this.params = params;
	}
	
	public Map<String, String> getControls() {
		return controls;
	}
	
	public void setControls(Map<String, String> controls) {
		this.controls = controls;
	}
	
	public void run(RobotSuite suite, RobotScenario scenario) {
		Service service = suite.getServices().get(getService());
		if (service != null) {			
			String fullURL = MessageFormat.format(suite.replaceProperties(service.getUrl()), suite.replaceProperties(getParams())).toString();
			suite.getLogger().debug("-----");
			suite.getLogger().debug("Executing service : " + fullURL);
			
			Map<String,String> requestHeaders = new HashMap<String,String>();
			if (service.getHeaders() != null) {
				for (String header : service.getHeaders().keySet()) {
					String value = suite.replaceProperties(service.getHeaders().get(header));
					if (value != null) {
						requestHeaders.put(header, value);
						suite.getLogger().debug("using header " + header + " = " + value);
					}
				}
			}
			if (getHeaders() != null) {
				for (String header : getHeaders().keySet()) {
					String value = suite.replaceProperties(getHeaders().get(header));
					if (value != null) {
						requestHeaders.put(header, value);
						suite.getLogger().debug("using header " + header + " = " + value);
					}
				}			
			}
			
			RobotHttp rp = new RobotHttp();
			
			if ("get".equalsIgnoreCase(service.getMethod()) == true) {
				rp.get(fullURL, requestHeaders);
			}
			else if ("post".equalsIgnoreCase(service.getMethod()) == true) {
				rp.post(fullURL, requestHeaders, suite.replaceProperties(getData()));
			}
			else if ("delete".equalsIgnoreCase(service.getMethod()) == true) {
				rp.delete(fullURL, requestHeaders);
			}
			
			boolean stepOk = rp.getResponse().getStatusLine().getStatusCode() == getStatus();
			String failedControl = null;
			
			if (stepOk && rp.getContent() != null) {				
				if (controls != null) {
					for (String jsonPath : controls.keySet()) {
						String expectedValue = controls.get(jsonPath);
						String actualValue = JsonPath.read(rp.getContent(), jsonPath).toString();
						if (expectedValue.startsWith("rxp:") && Pattern.matches(expectedValue.substring("rxp:".length()), actualValue) == false) {
							stepOk = false;
							failedControl = jsonPath;
							break;
						}
						else if (expectedValue.equals(actualValue) == false) {
							stepOk = false;
							failedControl = jsonPath;
							break;
						}
					}
				}
			}
			
			suite.getLogger().debug(stepOk == true ? "Response is as expected" : "Response has failed" + (failedControl == null ? "" : " on control : " + failedControl));
			if (stepOk == false) suite.getLogger().error("Response has failed" + (failedControl == null ? "" : " on control : " + failedControl));
			suite.getLogger().debug("Status : " + rp.getResponse().getStatusLine().getStatusCode());
			if (rp.getContent() != null) {
				suite.getLogger().debug("Content : " + rp.getContent());
			}
	
			if (service.getResults() != null && rp.getResponse().getStatusLine().getStatusCode() >= 200 && rp.getResponse().getStatusLine().getStatusCode() < 300) {
				for (String property : service.getResults().keySet()) {
					String jsonPath = service.getResults().get(property);
					String value = JsonPath.read(rp.getContent(), jsonPath);
					if (value != null) {
						suite.getProperties().put(property, value);
						suite.getLogger().debug("Property " + property + " has been set to " + value);
					}
				}
			}
		}
		else {
			suite.getLogger().warn("Warning : service not found => " + getService());
		}
	}
}
