package com.cwk.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cwk.codec.ICodec;
import com.cwk.codec.impl.IntegerCodec;
import com.cwk.codec.impl.ObjectCodec;
import com.cwk.codec.impl.StringCodec;
import com.test.mess.Message;
import com.test.mess.TestMessage;

import io.netty.buffer.ByteBuf;

/**
 *  编解码器
 * @author 3461
 */
public class Codec {
	
	 private final static Class<?>[] parents = new Class<?>[] {Message.class};
	 private final static String PACK = "com.test";
	 private static Map<Integer, Class<?>> mess = new HashMap<>();
	 private static Map<Class<?>, ICodec> allCodec = new HashMap<>();
	 
	 
	 static {
		 mess.put(1, TestMessage.class);
		 
		 allCodec.put(String.class, new StringCodec());
		 allCodec.put(Integer.class, new IntegerCodec());
		 allCodec.put(int.class, new IntegerCodec());
		 allCodec.put(Object.class, new ObjectCodec());
	 }
	
	public static void init() {
		List<Class<?>> classes = ClassUtil.scannerPack(PACK, new ClassFilterImpl(parents));
		
		
	}
	
	public static void main(String[] args) {
		
		Class<?> clazz = mess.get(1);
		encode(null, clazz);
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
