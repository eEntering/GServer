package com.game.utils;

/**
 * @author caiweikai
 * @date 2019年3月20日
 */
public class DispatchUtil {

	public static int getDispatchId(int line, int map) {
		if (map == 0) {
			return line;
		}
		return map * 10000000 + line;
	}
}
