package com.game.utils;

/**
 * @author caiweikai
 * @date 2019年3月20日
 */
public class StringUtils {
	
	public final static String EMPTY = "";
	
	
	public static boolean isEmpty(String string) {
		return string == null || string.trim().equals("");
	}
}
