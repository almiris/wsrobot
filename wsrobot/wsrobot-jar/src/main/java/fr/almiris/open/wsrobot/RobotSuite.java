package fr.almiris.open.wsrobot;

import java.util.List;
import java.util.Map;

public class RobotSuite {
	private Map<String,String> properties;
	private Map<String, Service> services;
	private List<RobotScenario> scenarios;
	private Logger logger = new DefaultLogger();
	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, Service> getServices() {
		return services;
	}

	public void setServices(Map<String, Service> services) {
		this.services = services;
	}

	public List<RobotScenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<RobotScenario> scenarios) {
		this.scenarios = scenarios;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void run() {
		int scount = 0;
		long start = System.currentTimeMillis();
		try {
			if (scenarios != null) {
				for (RobotScenario scenario : scenarios) {
					if (scenario.isActive()) {
						scenario.run(this);
						scount++;
					}
				}
			}
		}
		catch (Exception e) {
			getLogger().error("Exception : " + e.toString());
		}
		finally {
			getLogger().debug(scount + " scenario(s) executed in " + (System.currentTimeMillis() - start) + " ms");
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
