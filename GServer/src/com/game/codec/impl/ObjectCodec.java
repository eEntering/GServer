package com.game.codec.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.game.codec.ICodec;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;

/** 
 * 对象编解码器,包括message和bean
 *  */
public class ObjectCodec implements ICodec {

	/** 消息对象的每个字段 */
	private final List<Field> fields = new ArrayList<>();
	/** 每个字段对应的泛型类型，列表才会有 */
	private final List<Class<?>> genericClasses = new ArrayList<>();
	/** 每个字段类型的编解码器 */
	private final List<ICodec> codeces = new ArrayList<>();
	
	@Override
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		try {
			Object object = clazz.newInstance();
			for (int index = 0, length = fields.size(); index < length; index++) {
				Field field = fields.get(index);
				field.setAccessible(true);
				Class<?> fieldType = field.getType();
				Class<?> genericClazz = genericClasses.get(index);
				ICodec codec = codeces.get(index);
				if (codec == null) {
					logger.error("找不到编解码器:" + fieldType.getName());
					return null;
				}
				Object value = codec.read(in, fieldType, genericClazz);
				field.set(object, value);
			}
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		try {
			for (int index = 0, length = fields.size(); index < length; index++) {
				Field field = fields.get(index);
				field.setAccessible(true);
				Class<?> fieldType = field.getType();
				Class<?> genericClazz = genericClasses.get(index);
				ICodec codec = codeces.get(index);
				if (codec == null) {
					logger.error("找不到编解码器:" + fieldType.getName());
					return false;
				}
				Object obj;
				obj = field.get(value);
				codec.write(in, obj, fieldType, genericClazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public Class<?>[] getBindClasses() {
		return null;
	}

	public List<Field> getFields() {
		return fields;
	}

	public List<Class<?>> getGenericClasses() {
		return genericClasses;
	}

	public List<ICodec> getCodeces() {
		return codeces;
	}
}
