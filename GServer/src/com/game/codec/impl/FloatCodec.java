package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class FloatCodec implements ICodec {

	@Override
	public Object read(ByteBuf in, Class<?> clazz) {
		return in.readFloat();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz) {
		in.writeFloat((Float) value);
		return true;
	}
}
