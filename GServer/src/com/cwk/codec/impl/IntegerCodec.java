package com.cwk.codec.impl;

import com.cwk.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class IntegerCodec implements ICodec {

	
	public Object read(ByteBuf in, Class<?> clazz) {
		return in.readInt();
	}

	public boolean write(ByteBuf in, Object value, Class<?> clazz) {
		in.writeInt((Integer) value);
		return true;
	}
}
