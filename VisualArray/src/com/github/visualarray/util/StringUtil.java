package com.github.visualarray.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {
	private static final Pattern DEFUALT_DELIMITER = Pattern.compile("\\s");

	public static String capitalize(String s) {
		return capitalize(s, null, true);
	}

	public static String capitalize(String s, String delimiter,
			boolean capitalizeBeginning) {
		if (s.isEmpty())
			return s;

		Pattern p = delimiter != null ? Pattern.compile(delimiter)
				: DEFUALT_DELIMITER;
		char[] chars = s.toCharArray();
		if (capitalizeBeginning) {
			chars[0] = Character.toUpperCase(chars[0]);
		}
		Matcher m = p.matcher(s);
		while (m.find()) {
			int index = m.end();
			char upperCh = Character.toUpperCase(s.charAt(index));
			chars[index] = upperCh;
		}
		return String.valueOf(chars);
	}

	private StringUtil() {
	}
}
