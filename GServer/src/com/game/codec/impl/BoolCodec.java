package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class BoolCodec implements ICodec {

	@Override
	public Object read(ByteBuf in, Class<?> clazz) {
		return in.readBoolean();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz) {
		in.writeBoolean((Boolean) value);
		return true;
	}
}
