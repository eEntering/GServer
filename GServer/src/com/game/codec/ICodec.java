package com.game.codec;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBuf;

/**
 * 编解码器接口
 * @author caiweikai
 * @date 2019年3月18日
 */
public interface ICodec {
	
	static Logger logger = LoggerFactory.getLogger(ICodec.class);
	
	/**
	 * @param in 
	 * @param clazz 字段类型
	 * @param genericClass 字段泛型类型，这种类型只有列表、数组和map会有
	 */
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass);

	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass);
	
	public Class<?>[] getBindClasses();
	
	/** 注册基本数据类型及其包装类型的编解码器 
	 * @param multiCodec */
	default void register(Map<Class<?>, ICodec> singleCodec, Map<Class<?>, ICodec> multiCodec) {
		if (getBindClasses() == null) {
			multiCodec.put(getClass(), this);
			logger.error("注册编解码器：" + getClass().getName() + " -- " + getClass().getName());
		} else {
			for (Class<?> clazz : getBindClasses()) {
				singleCodec.put(clazz, this);
				logger.error("注册编解码器：" + clazz.getName() + " -- " + getClass().getName());
			}
		}
	}
}
