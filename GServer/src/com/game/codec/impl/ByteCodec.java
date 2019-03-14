package com.game.codec.impl;

import com.game.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class ByteCodec implements ICodec {

	@Override
	public Object read(ByteBuf in, Class<?> clazz) {
		return in.readByte();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz) {
		in.writeByte((Byte) value);
		return true;
	}

}
