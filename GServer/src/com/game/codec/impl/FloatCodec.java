package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class FloatCodec implements ICodec {

	private Class<?>[] bindClasses = new Class[] { Float.class, float.class };
	
	@Override
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		return in.readFloat();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		in.writeFloat((Float) value);
		return true;
	}

	@Override
	public Class<?>[] getBindClasses() {
		return  bindClasses;
	}
}
