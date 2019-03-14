package com.game.codec.impl;

import java.lang.reflect.Field;

import com.game.codec.ICodec;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;

/** 
 * 对象编解码器
 *  */
public class ObjectCodec implements ICodec {

	@Override
	public Object read(ByteBuf in, Class<?> clazz) {
		try {
			Object object = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Class<?> fieldType = field.getType();
				ICodec codec = Codec.getCodec(fieldType);
				if (codec == null) {
					logger.error("找不到编解码器");
					return null;
				}
				Object value = codec.read(in, fieldType);
				field.set(object, value);
			}
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz) {
		try {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Class<?> fieldType = field.getType();
				ICodec codec = Codec.getCodec(fieldType);
				if (codec == null) {
					logger.error("找不到编解码器");
					return false;
				}
				Object obj;
				obj = field.get(value);
				codec.write(in, obj, fieldType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
