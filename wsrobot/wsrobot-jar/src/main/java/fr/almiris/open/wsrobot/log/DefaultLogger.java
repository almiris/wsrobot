package fr.almiris.open.wsrobot.log;

public class DefaultLogger implements Logger {

	@Override
	public void debug(String str) {
		System.out.println(str);
	}

	@Override
	public void info(String str) {
		System.out.println(str);
	}

	@Override
	public void warn(String str) {
		System.err.println(str);
	}

	@Override
	public void error(String str) {
		System.err.println(str);
	}

}
