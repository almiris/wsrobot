package fr.almiris.open.wsrobot;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;

public class RobotUnit {
	private String service;
	private Map<String,String> headers;
	private String[] params;
	private String data;
	private int status;
	
	public RobotUnit() {
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
	
	public void run(RobotSuite suite) {
		Service service = suite.getServices().get(getService());
		String fullURL = MessageFormat.format(suite.getBaseURL() + service.getUrl(), suite.replaceProperties(getParams())).toString();
		System.out.println("Executing service : " + fullURL);
		

		Map<String,String> requestHeaders = new HashMap<String,String>();
		if (service.getHeaders() != null) {
			for (String header : service.getHeaders().keySet()) {
				String value = suite.replaceProperties(service.getHeaders().get(header));
				if (value != null) {
					requestHeaders.put(header, value);
					System.out.println("header " + header + " = " + value);
				}
			}
		}
		if (getHeaders() != null) {
			for (String header : getHeaders().keySet()) {
				String value = suite.replaceProperties(getHeaders().get(header));
				if (value != null) {
					requestHeaders.put(header, value);
					System.out.println("header " + header + " = " + value);
				}
			}			
		}
		
		RobotHttp rp = new RobotHttp();
		
		if ("get".equalsIgnoreCase(service.getMethod()) == true) {
			rp.get(fullURL, requestHeaders);
		}
		else if ("post".equalsIgnoreCase(service.getMethod()) == true) {
			rp.post(fullURL, requestHeaders, getData());
		}
		else if ("delete".equalsIgnoreCase(service.getMethod()) == true) {
			rp.delete(fullURL, requestHeaders);
		}
		
		System.out.println("Status : " + rp.getResponse().getStatusLine().getStatusCode());
		System.out.println("Response : " + rp.getContent());
		System.out.println("Result : " + (rp.getResponse().getStatusLine().getStatusCode() == getStatus() ? "ok" : "ko"));

		if (service.getResults() != null && rp.getResponse().getStatusLine().getStatusCode() >= 200 && rp.getResponse().getStatusLine().getStatusCode() < 300) {
			for (String property : service.getResults().keySet()) {
				String jsonPath = service.getResults().get(property);
				String value = JsonPath.read(rp.getContent(), jsonPath);
				if (value != null) {
					suite.getProperties().put(property, value);
					System.out.println("Property " + property + " = " + value);
				}
			}
		}
	}
}
