package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class IntegerCodec implements ICodec {

	private Class<?>[] bindClasses = new Class[] { Integer.class, int.class };
	
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		return in.readInt();
	}

	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		in.writeInt((Integer) value);
		return true;
	}
	
	public Class<?>[] getBindClasses() {
		return bindClasses;
	}
}
