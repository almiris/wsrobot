package fr.almiris.open.wsrobot.log;

public interface Logger {
	public void debug(String str);
	public void info(String str);
	public void warn(String str);
	public void error(String str);
}
