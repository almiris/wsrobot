package fr.almiris.open.wsrobot;

import java.util.List;
import java.util.Map;

public class RobotSuite {
	private String baseURL;
	/**
	 * Not synchronized... at all !
	 */
	private Map<String,String> properties;
	private Map<String, Service> services;
	private List<RobotUnit> tests;

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public Map<String, Service> getServices() {
		return services;
	}

	public void setServices(Map<String, Service> services) {
		this.services = services;
	}

	public List<RobotUnit> getTests() {
		return tests;
	}

	public void setTests(List<RobotUnit> tests) {
		this.tests = tests;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void run() {
		int count = 0;
		try {
			if (tests != null) {
				for (RobotUnit test : tests) {
					test.run(this);
					count++;
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception : " + e.toString());
		}
		finally {
			System.out.println(count + " test(s) executed");
		}
	}
	
	public String replaceProperties(String str) {
		String result = str;
		if (properties != null && str != null) {
			for (String property : properties.keySet()) {
				result = result.replace("$" + property + "$", properties.get(property));
			}
		}
		return result;
	}

	public String[] replaceProperties(String[] strArr) {
		if (strArr == null) {
			return null;
		}
		
		String[] result = new String[strArr.length];
		
		for (int i = 0; i < strArr.length; i++) {
			result[i] = replaceProperties(strArr[i]);
		}
		
		return result;
	}
}
