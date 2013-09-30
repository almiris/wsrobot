package fr.almiris.open.wsrobot.conf;

import java.util.List;
import java.util.Map;

public class RobotSuiteConf {
	
	private String name;
	private Map<String,String> properties;
	private Map<String, RobotServiceConf> services;
	private List<RobotScenarioConf> scenarios;
	
	public RobotSuiteConf() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, RobotServiceConf> getServices() {
		return services;
	}

	public void setServices(Map<String, RobotServiceConf> services) {
		this.services = services;
	}

	public List<RobotScenarioConf> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<RobotScenarioConf> scenarios) {
		this.scenarios = scenarios;
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
