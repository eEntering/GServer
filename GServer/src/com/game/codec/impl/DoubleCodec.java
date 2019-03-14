package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class DoubleCodec implements ICodec {

	@Override
	public Object read(ByteBuf in, Class<?> clazz) {
		return in.readDouble();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz) {
		in.writeDouble((Double) value);
		return true;
	}

}
