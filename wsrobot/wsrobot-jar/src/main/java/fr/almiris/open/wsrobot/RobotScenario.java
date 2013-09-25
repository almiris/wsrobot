package fr.almiris.open.wsrobot;

import java.util.List;

public class RobotScenario {
	private String name;
	private boolean active;
	private List<RobotStep> steps;
	
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
	
	public List<RobotStep> getSteps() {
		return steps;
	}
	
	public void setSteps(List<RobotStep> steps) {
		this.steps = steps;
	}
	
	public void run(RobotSuite suite) {
		int scount = 0;
		long start = System.currentTimeMillis();
		try {
			if (steps != null) {
				for (RobotStep step : steps) {
					step.run(suite, this);
					scount++;
				}
			}
		}
		catch (Exception e) {
			suite.getLogger().error("Exception : " + e.toString());
		}
		finally {
			suite.getLogger().debug(scount + " step(s) executed in " + (System.currentTimeMillis() - start) + " ms");
		}		
	}
}
