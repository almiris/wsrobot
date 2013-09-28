package fr.almiris.open.wsrobot.conf;

import java.util.List;

public class RobotScenarioConf {
	private String name;
	private boolean active;
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
	
	public List<RobotStepConf> getSteps() {
		return steps;
	}
	
	public void setSteps(List<RobotStepConf> steps) {
		this.steps = steps;
	}

}
