package fr.almiris.open.wsrobot.conf;

import java.util.List;

public class RobotScenarioConf {
	private String name;
	private boolean active = true;
	private boolean unique = false;
	private boolean multiple = false;
	
	private List<RobotStepConf> steps;
	
	public RobotScenarioConf() {	
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public List<RobotStepConf> getSteps() {
		return steps;
	}
	
	public void setSteps(List<RobotStepConf> steps) {
		this.steps = steps;
	}

}
