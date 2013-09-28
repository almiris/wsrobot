package fr.almiris.open.wsrobot.util;

import java.io.StringWriter;

public class StringTool {

	public static String pad(int value, int len) {
		String str = String.valueOf(value);
		StringWriter sw = new StringWriter();
		sw.append(str);
		for (int i = str.length(); i < len; i++) {
			sw.append(" ");
		}
		return sw.toString();
	}

}
