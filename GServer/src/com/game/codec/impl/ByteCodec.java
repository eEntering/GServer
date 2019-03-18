package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class ByteCodec implements ICodec {

	private Class<?>[] bindClasses = new Class[] { Byte.class, byte.class };
	
	@Override
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		return in.readByte();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		in.writeByte((Byte) value);
		return true;
	}

	@Override
	public Class<?>[] getBindClasses() {
		return bindClasses;
	}

}
