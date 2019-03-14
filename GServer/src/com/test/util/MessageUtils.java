package com.test.util;

import io.netty.buffer.ByteBuf;

public class MessageUtils {

	/** 释放引用计数对象 **/
	public static void release(Object msg) {
		if (msg instanceof ByteBuf) {
			((ByteBuf) msg).release();
		}
	}
}
