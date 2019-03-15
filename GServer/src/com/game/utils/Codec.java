package com.game.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.codec.ICodec;
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
	
	 private final static Class<?>[] parents = new Class<?>[] {Message.class};
	 private final static String PACK = "com.game.";
	 private static Map<Integer, Class<?>> messageMap = new HashMap<>();
	 private static Map<Class<?>, ICodec> allCodec = new HashMap<>();
	 
	 
	 static {
		 
		 allCodec.put(String.class, new StringCodec());
		 allCodec.put(Integer.class, new IntegerCodec());
		 allCodec.put(int.class, new IntegerCodec());
		 allCodec.put(Object.class, new ObjectCodec());
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
						LOGGER.error("协议号重复！！！！！mid: {}, className: {} - {}", new Object[] { mid, clazz.getName(), existClass.getName() });
						LOGGER.error("协议号重复！！！！！mid: {}, className: {} - {}", new Object[] { mid, clazz.getName(), existClass.getName() });
						LOGGER.error("协议号重复！！！！！mid: {}, className: {} - {}", new Object[] { mid, clazz.getName(), existClass.getName() });
						System.exit(0);
					}
					
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		LOGGER.error("协议加载成功！！！！");
	}
	
	public static void main(String[] args) {
		
//		Class<?> clazz = mess.get(1);
//		encode(null, clazz);
		init();
	}
	
	public static void encode(ByteBuf buf, Class<?> clazz) {
		try {
			
			Field[] fields = clazz.getDeclaredFields();
			
//			 Object object = (Object)clazz.newInstance();
//			Field[] fields2 = object.getClass().getDeclaredFields();
			for(Field field : fields) {
				Class<?> fieldType = field.getType();
				ICodec codec = allCodec.get(fieldType);
				if (codec == null) {
					System.out.println("找不到解码器！！！类型："+fieldType.getName());
					return;
				}
//				Object value = field.get(obj)
//				codec.write(buf, field.get)
			}
			
			
			System.out.println(fields.length);
			
		} catch ( Exception e) {
			e.printStackTrace();
		}  
		
		
	}
	
	
	public static ICodec getCodec(Class<?> claszz) {
		return allCodec.get(claszz);
	}
}
