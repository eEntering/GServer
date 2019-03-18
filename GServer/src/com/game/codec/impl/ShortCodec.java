package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class ShortCodec implements ICodec {

	private Class<?>[] bindClasses = new Class[] { Short.class, short.class };
	
	@Override
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		return in.readShort();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		in.writeShort((Short) value);
		return true;
	}

	@Override
	public Class<?>[] getBindClasses() {
		return bindClasses;
	}
}
