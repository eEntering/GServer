package com.game.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBuf;

public interface ICodec {
	
	static Logger logger = LoggerFactory.getLogger(ICodec.class);
	
	public Object read(ByteBuf in, Class<?> clazz);

	public boolean write(ByteBuf in, Object value, Class<?> clazz);
}
