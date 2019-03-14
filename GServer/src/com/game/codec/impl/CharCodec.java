package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class CharCodec implements ICodec {

	@Override
	public Object read(ByteBuf in, Class<?> clazz) {
		return in.readChar();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz) {
		in.writeChar((Character) value);
		return true;
	}

}
