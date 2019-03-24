package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class StringCodec implements ICodec {

	private Class<?>[] bindClasses = new Class[] { String.class };
	
	@Override
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		int length = in.readInt();
		byte[] bytes = new byte[length];
		in.readBytes(bytes);
		// bytes = in.readBytes(bytes);
		return new String(bytes);
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		if (value == null) {
			in.writeInt(0);
			return true;
		}
		String string = (String) value;
		byte[] bytes = string.getBytes();
		in.writeInt(bytes.length);
		in.writeBytes(bytes);
		return true;
	}

	@Override
	public Class<?>[] getBindClasses() {
		return bindClasses;
	}

}
