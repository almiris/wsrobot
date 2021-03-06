package fr.almiris.open.wsrobot.conf;

import java.util.Map;

public class RobotStepConf {
	private String description;
	private String service;
	private Map<String,String> headers;
	private String[] params;
	private String data;
	private int status;
	private Map<String,String> jcontrols;
	private Map<String,String> pcontrols;
	private Map<String,String> presults;
	private RobotSQLConf sql;
	
	public RobotStepConf() {
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String[] getParams() {
		return params;
	}
	
	public void setParams(String[] params) {
		this.params = params;
	}

	public Map<String, String> getJcontrols() {
		return jcontrols;
	}

	public void setJcontrols(Map<String, String> jcontrols) {
		this.jcontrols = jcontrols;
	}

	public Map<String, String> getPcontrols() {
		return pcontrols;
	}

	public void setPcontrols(Map<String, String> pcontrols) {
		this.pcontrols = pcontrols;
	}

	public Map<String, String> getPresults() {
		return presults;
	}

	public void setPresults(Map<String, String> presults) {
		this.presults = presults;
	}
	public RobotSQLConf getSql() {
		return sql;
	}
	public void setSql(RobotSQLConf sql) {
		this.sql = sql;
	}

}
