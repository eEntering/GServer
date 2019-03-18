package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class BoolCodec implements ICodec {

	private Class<?>[] bindClasses = new Class[] { Boolean.class, boolean.class };
	
	@Override
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		return in.readBoolean();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		in.writeBoolean((Boolean) value);
		return true;
	}

	@Override
	public Class<?>[] getBindClasses() {
		return bindClasses;
	}
}
