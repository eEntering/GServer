package com.game.codec.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.game.codec.ICodec;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;

public class CollectionCodec implements ICodec {

	@Override
	public Object read(ByteBuf in, Class<?> clazz, Class<?> genericClass) {
		short length = (short) Codec.getCodec(Short.class).read(in, null, null);
		Collection<Object> collections = null;
		int modifier = clazz.getModifiers();
		if (Modifier.isInterface(modifier) || Modifier.isAbstract(modifier)) {
			if (Set.class.isAssignableFrom(clazz)) {
				collections = new HashSet<>();
			} else if (List.class.isAssignableFrom(clazz)) {
				collections = new ArrayList<>();
			}
		} else {
			try {
				collections = (Collection<Object>) clazz.newInstance();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		if (collections == null) {
			return null;
		}

		for (int i = 0; i < length; i++) {
			ICodec codec = Codec.getCodec(genericClass);
			Object element = codec.read(in, genericClass, null);
			collections.add(element);
		}
		return collections;
	}

	@Override
	public boolean write(ByteBuf in, Object value, Class<?> clazz, Class<?> genericClass) {
		if (value == null) {
			Codec.getCodec(Short.class).write(in, 0, null, null);
			return true;
		}
		Collection<Object> collections = (Collection<Object>) value;
		Codec.getCodec(Short.class).write(in, (short) collections.size(), null, null);
		
		for (Object object : collections) {
			ICodec codec = Codec.getCodec(genericClass);
			codec.write(in, object, genericClass, null);
		}
		return true;
	}

	@Override
	public Class<?>[] getBindClasses() {
		return null;
	}

}
