package com.cwk.codec.impl;

import com.cwk.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class LongCodec implements ICodec {

	@Override
	public Object read(ByteBuf in, Class<?> clazz) {
		return in.readLong();
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz) {
		in.writeLong((Long) value);
		return true;
	}

}
