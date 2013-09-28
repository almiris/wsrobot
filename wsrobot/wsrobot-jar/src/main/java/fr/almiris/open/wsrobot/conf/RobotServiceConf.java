package fr.almiris.open.wsrobot.conf;

import java.util.Map;

public class RobotServiceConf {
	
	private String name;
	private String url;
	private String method;
	private Map<String,String> headers;
	private Map<String,String> jresults;

	public RobotServiceConf() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public Map<String, String> getJresults() {
		return jresults;
	}
	public void setJresults(Map<String, String> jresults) {
		this.jresults = jresults;
	}

}
