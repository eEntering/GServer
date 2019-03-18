package com.game.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.codec.ICodec;
import com.game.codec.impl.CollectionCodec;
import com.game.codec.impl.IntegerCodec;
import com.game.codec.impl.ObjectCodec;
import com.game.codec.impl.StringCodec;
import com.game.message.Message;
import com.game.message.TestMessage;

import io.netty.buffer.ByteBuf;

/**
 *  编解码器
 * @author caiweikai
 */
public class Codec {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Codec.class);
	
	 private final static Class<?>[] parents = new Class<?>[] {Message.class,ICodec.class};
	 private final static String PACK = "com.game.";
	 private static Map<Integer, Class<?>> messageMap = new HashMap<>();
	 /** 基本数据类型及其包装类型的编解码器，还有协议的  */
	 private static Map<Class<?>, ICodec> singleCodec = new HashMap<>();
	 /** 列表和数组的编解码器 */
	 private static Map<Class<?>, ICodec> multiCodec = new HashMap<>();
	 
	 
	 static {
		 
//		 allCodec.put(String.class, new StringCodec());
//		 allCodec.put(Integer.class, new IntegerCodec());
//		 allCodec.put(int.class, new IntegerCodec());
//		 allCodec.put(Object.class, new ObjectCodec());
	 }
	
	public static void init() {
		List<Class<?>> classes = ClassUtil.scannerPack(PACK, new ClassFilterImpl(parents));
		try {
			for (Class<?> clazz : classes) {
				if (Message.class.isAssignableFrom(clazz)) {
					Message message = (Message) clazz.newInstance();
					int mid = message.getMessageId();
					Class<?> existClass = null;
					if ((existClass = messageMap.put(mid, clazz)) != null) {
						LOGGER.error("协议号重复！！！！！mid: {}, className: {} - {}",
								new Object[] { mid, clazz.getName(), existClass.getName() });
						LOGGER.error("协议号重复！！！！！mid: {}, className: {} - {}",
								new Object[] { mid, clazz.getName(), existClass.getName() });
						LOGGER.error("协议号重复！！！！！mid: {}, className: {} - {}",
								new Object[] { mid, clazz.getName(), existClass.getName() });
						System.exit(0);
					}
					
					ICodec codec = getCodec(clazz);
					if (codec == null) {
						LOGGER.error("协议找不到编解码器！！！！！mid: {}, className: {} ",
								new Object[] { mid, clazz.getName() });
						System.exit(0);
					}
					LOGGER.error("注册编解码器,mid:{}, {} ", new Object[] { mid, clazz.getName() });
				} else if (ICodec.class.isAssignableFrom(clazz)) {
					ICodec codec = (ICodec) clazz.newInstance();
					codec.register(singleCodec, multiCodec);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		LOGGER.error("协议加载完成！！！！");
	}
	
	public static void main(String[] args) {
		
//		Class<?> clazz = mess.get(1);
//		encode(null, clazz);
		init();
	}
	
	public static Class<?> getGenericClass(Field field) {
		try {
			Class<?> clazz = field.getType();
			if (Collection.class.isAssignableFrom(clazz)) {
				return (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
			} else if (clazz.isArray()) {
				return clazz.getComponentType();
			}
		} catch (Exception e) {
			LOGGER.error("获取泛型出错！" + e.getMessage());
		}
		return null;
	}
	
	/** 编码成二进制 */
	public static void encode(ByteBuf buf, Object object) {
		Class<?> clazz = object.getClass();
		if (!Message.class.isAssignableFrom(clazz)) {
			LOGGER.error("对象不是消息类型！！！！" + clazz.getName());
			return;
		}
		try {
			buf.writeByte((byte) 0); // 占位符

			int mid = ((Message) object).getMessageId();
			buf.writeInt(mid);

			ICodec codec = getCodec(clazz);
			if (codec == null) {
				LOGGER.error("找不到编解码器！！！Class:" + clazz.getName());
				return;
			}
			codec.write(buf, object, null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 解码成对象 */
	public static Object decode(ByteBuf buf) {
		Object object = null;
		try {
			byte i = buf.readByte(); // 占位符

			int mid = buf.readInt();
			Class<?> clazz = messageMap.get(mid);
			if (clazz == null) {
				LOGGER.error("消息找不到类！！！mid：" + mid);
				return object;
			}

			ICodec codec = getCodec(clazz);
			if (codec == null) {
				LOGGER.error("找不到编解码器！！！Class:" + clazz.getName());
				return object;
			}
			object = codec.read(buf, clazz, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}
	
	
	public static ICodec getCodec(Class<?> clazz) {
		ICodec codec = singleCodec.get(clazz);
		if (codec != null) {
			return codec;
		}
		if (Collection.class.isAssignableFrom(clazz)) {
			return multiCodec.get(CollectionCodec.class);
		}
		synchronized (singleCodec) {
			if ((codec = singleCodec.get(clazz)) != null) {
				return codec;
			}
			ObjectCodec objectCodec = new ObjectCodec();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				Class<?> fieldType = field.getType();
				Class<?> gnericClass = getGenericClass(field);
				objectCodec.getFields().add(field);
				objectCodec.getCodeces().add(getCodec(fieldType));
				objectCodec.getGenericClasses().add(gnericClass);
			}
			singleCodec.put(clazz, objectCodec);
			
			return objectCodec;
		}
	}
}
