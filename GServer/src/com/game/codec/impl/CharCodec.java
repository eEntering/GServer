package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class CharCodec implements ICodec {

	private Class<?>[] bindClasses = new Class[] { Character.class, char.class };
	
	@Override
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		return in.readChar();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		in.writeChar((Character) value);
		return true;
	}

	@Override
	public Class<?>[] getBindClasses() {
		return bindClasses;
	}

}
