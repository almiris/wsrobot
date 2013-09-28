package fr.almiris.open.wsrobot.report;

import java.util.ArrayList;
import java.util.List;

import fr.almiris.open.wsrobot.RobotSuite;
import fr.almiris.open.wsrobot.conf.RobotSuiteConf;

public class RobotSuiteReport {

	RobotSuiteConf conf;
	List<RobotScenarioReport> scenarioReports;
	RobotSuite.Status status;
	int successCount;
	int errorCount;
	long executionTime;
	String exception;
	
	public RobotSuiteReport() {	
	}

	public RobotSuiteConf getConf() {
		return conf;
	}

	public void setConf(RobotSuiteConf conf) {
		this.conf = conf;
	}

	public List<RobotScenarioReport> getScenarioReports() {
		return scenarioReports;
	}

	public void setScenarioReports(List<RobotScenarioReport> scenarioReports) {
		this.scenarioReports = scenarioReports;
	}

	public RobotSuite.Status getStatus() {
		return status;
	}

	public void setStatus(RobotSuite.Status status) {
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

	public synchronized void addScenarioReport(RobotScenarioReport report) {
		if (this.scenarioReports == null) {
			this.scenarioReports = new ArrayList<RobotScenarioReport>();
		}
		this.scenarioReports.add(report);
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}	
	
}
