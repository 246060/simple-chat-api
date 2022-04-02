package xyz.jocn.chat.common.util;

import org.apache.logging.log4j.util.Strings;

public class StringUtil {
	public static boolean isNotBlank(String txt) {
		return Strings.isNotBlank(txt);
	}

	public static boolean isBlank(String txt) {
		return Strings.isBlank(txt);
	}
}
