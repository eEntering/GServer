package com.game.utils;

/**
 * @author caiweikai
 * @date 2019年3月20日
 */
public class DispatchUtil {

	public static int getDispatchId(int line, int map) {
		return line * 10000000 + map;
	}
}
