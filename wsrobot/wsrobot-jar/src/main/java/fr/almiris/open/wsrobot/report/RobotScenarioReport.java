package fr.almiris.open.wsrobot.report;

import java.util.ArrayList;
import java.util.List;

import fr.almiris.open.wsrobot.RobotScenario;
import fr.almiris.open.wsrobot.conf.RobotScenarioConf;

public class RobotScenarioReport {

	RobotScenarioConf conf;
	List<RobotStepReport> stepReports;
	RobotScenario.Status status;
	int successCount;
	int errorCount;
	long executionTime;
	String exception;
	
	public RobotScenarioReport() {	
	}

	
	public RobotScenarioConf getConf() {
		return conf;
	}


	public void setConf(RobotScenarioConf conf) {
		this.conf = conf;
	}


	public List<RobotStepReport> getStepReports() {
		return stepReports;
	}

	public void setStepReports(List<RobotStepReport> stepReports) {
		this.stepReports = stepReports;
	}

	public RobotScenario.Status getStatus() {
		return status;
	}

	public void setStatus(RobotScenario.Status status) {
		this.status = status;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	
	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public synchronized void addStepReport(RobotStepReport report) {
		if (this.stepReports == null) {
			this.stepReports = new ArrayList<RobotStepReport>();
		}
		this.stepReports.add(report);
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
}
