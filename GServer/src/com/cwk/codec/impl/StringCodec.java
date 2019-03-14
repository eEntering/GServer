package com.cwk.codec.impl;

import com.cwk.codec.ICodec;

import io.netty.buffer.ByteBuf;

public class StringCodec implements ICodec {

	@Override
	public Object read(ByteBuf in, Class<?> clazz) {
		int length = in.readInt();
		byte[] bytes = new byte[length];
		 in.readBytes(bytes) ;
//		bytes = in.readBytes(bytes);
		return new String(bytes);
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz) {
		String string = (String) value;
		byte[] bytes = string.getBytes();
		in.writeInt(bytes.length);
		in.writeBytes(bytes);
		return true;
	}

}
