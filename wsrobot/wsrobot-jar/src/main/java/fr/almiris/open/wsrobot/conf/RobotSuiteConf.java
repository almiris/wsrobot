package fr.almiris.open.wsrobot.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotSuiteConf {
	
	private String name;
	private List<String> imports;
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

	public List<String> getImports() {
		return imports;
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
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
			boolean isReplace = true;
			while (isReplace) {
				isReplace = false;
				for (String property : properties.keySet()) {
					String toReplace = "$" + property + "$";
					if (result.indexOf(toReplace) != -1) {
						isReplace = true;
						result = result.replace("$" + property + "$", properties.get(property));
					}
				}
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

	public void mergeAndOverrideWith(RobotSuiteConf conf) {
		name = conf.getName();
		
		if (conf.getProperties() != null) {
			if (properties == null) {
				properties = new HashMap<String,String>(conf.getProperties());
			}
			else {
				properties.putAll(conf.getProperties());
			}
		}
		if (conf.getServices() != null) {
			if (services == null) {
				services = new HashMap<String,RobotServiceConf>(conf.getServices());
			}
			else {
				services.putAll(conf.getServices());
			}
		}
		if (conf.getScenarios() != null) {
			if (scenarios == null) {
				scenarios = new ArrayList<RobotScenarioConf>(conf.getScenarios());
			}
			else {
				scenarios.addAll(conf.getScenarios());
			}
		}
	}
}
