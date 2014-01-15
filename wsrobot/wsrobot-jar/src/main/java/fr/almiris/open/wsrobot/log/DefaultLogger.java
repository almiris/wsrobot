package fr.almiris.open.wsrobot.log;

public class DefaultLogger implements Logger {

	@Override
	public void debug(String str) {
		System.out.println(str);
		System.out.flush();
	}

	@Override
	public void info(String str) {
		System.out.println(str);
		System.out.flush();
	}

	@Override
	public void warn(String str) {
		System.err.println(str);
		System.err.flush();
	}

	@Override
	public void error(String str) {
		System.err.println(str);
		System.err.flush();
	}

}
