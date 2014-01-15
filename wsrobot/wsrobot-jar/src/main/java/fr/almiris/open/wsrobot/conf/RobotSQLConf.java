package fr.almiris.open.wsrobot.conf;

import java.util.List;

public class RobotSQLConf {

	String url;
	String user;
	String password;
	List<String> reqs;
	
	public RobotSQLConf() {		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getReqs() {
		return reqs;
	}

	public void setReqs(List<String> reqs) {
		this.reqs = reqs;
	}
}
