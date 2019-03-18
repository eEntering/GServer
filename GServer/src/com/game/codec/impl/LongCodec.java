package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class LongCodec implements ICodec {

	private Class<?>[] bindClasses = new Class[] { Long.class, long.class };
	
	@Override
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		return in.readLong();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		in.writeLong((Long) value);
		return true;
	}

	@Override
	public Class<?>[] getBindClasses() {
		return bindClasses;
	}

}
