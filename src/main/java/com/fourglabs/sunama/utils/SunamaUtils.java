package com.fourglabs.sunama.utils;

import java.io.Writer;

public class SunamaUtils {
	public static String getPrefix(String string) {
		String prefix = string;
		if (prefix.equalsIgnoreCase("Male")) {
			prefix = Constants.STR_SON_OF;
		} else {
			prefix = Constants.STR_DO_OF;
		}
		return prefix;
	}

	public static String correctName(String in) {
		String[] tokens = in.split(Constants.DELIM_SPACE);
		StringBuffer sb = new StringBuffer();
		if (tokens != null) {
			for (String str : tokens) {
				sb.append(str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase()).append(Constants.DELIM_SPACE);
			}
		}
		return sb.toString().trim();
	}

	public static String getOccupation(String occupation) {
		String ret = "";
		if (occupation != null && !occupation.isEmpty()) {
			ret = " and working as "+ occupation;
		}
		return ret;
	}
	
	public static String getStrOrEmpty(String in) {
		return in.isEmpty() ? Constants.STR_NA : in;
	}
}
