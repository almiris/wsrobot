package fr.almiris.open.wsrobot.report;

import java.util.HashMap;
import java.util.Map;

import fr.almiris.open.wsrobot.RobotStep;
import fr.almiris.open.wsrobot.conf.RobotStepConf;

public class RobotStepReport {

	RobotStepConf conf;
	RobotStep.Status status;
	long executionTime;
	Map<String,String> headers;
	int actualStatus;
	String actualContent;
	String failedControl;
	String failedControlExpectedValue;
	String failedControlActualValue;
	String resultNotFound;
	Map<String,String> properties;
	
	public RobotStepReport() {	
	}
	
	public RobotStepConf getConf() {
		return conf;
	}

	public void setConf(RobotStepConf conf) {
		this.conf = conf;
	}

	public RobotStep.Status getStatus() {
		return status;
	}

	public void setStatus(RobotStep.Status status) {
		this.status = status;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public int getActualStatus() {
		return actualStatus;
	}

	public void setActualStatus(int actualStatus) {
		this.actualStatus = actualStatus;
	}

	public String getActualContent() {
		return actualContent;
	}

	public void setActualContent(String actualContent) {
		this.actualContent = actualContent;
	}

	public String getFailedControl() {
		return failedControl;
	}

	public void setFailedControl(String failedControl) {
		this.failedControl = failedControl;
	}

	public String getFailedControlExpectedValue() {
		return failedControlExpectedValue;
	}

	public void setFailedControlExpectedValue(String failedControlExpectedValue) {
		this.failedControlExpectedValue = failedControlExpectedValue;
	}

	public String getFailedControlActualValue() {
		return failedControlActualValue;
	}

	public void setFailedControlActualValue(String failedControlActualValue) {
		this.failedControlActualValue = failedControlActualValue;
	}

	public String getResultNotFound() {
		return resultNotFound;
	}

	public void setResultNotFound(String resultNotFound) {
		this.resultNotFound = resultNotFound;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public synchronized void setProperty(String name, String value) {
		if (this.properties == null) {
			this.properties = new HashMap<String,String>();
		}
		this.properties.put(name, value);
	}
}
