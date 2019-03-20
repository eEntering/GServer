package com.game.socket;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.message.Message;
import com.game.message.MessageIds;
import com.game.message.annotation.MessageHandler;
import com.game.message.annotation.MessageID;
import com.game.message.annotation.MessageMethod;
import com.game.socket.dispatch.DefaultParamCovert;
import com.game.socket.dispatch.IGameDispatch;
import com.game.socket.dispatch.IParamCovert;
import com.game.utils.ClassFilter;
import com.game.utils.ClassUtil;
import com.game.utils.ContextUtil;
import com.game.utils.LinkStatus;

import io.netty.channel.Channel;

/**
 *  消息分发器
 * @author caiweikai
 * @date 2019年3月20日
 */
public class DispatchMessage {
	private final static Logger LOGGER = LoggerFactory.getLogger(DispatchMessage.class);
	
	private static DispatchMessage inst;
	private static IParamCovert paramCovert;
	
	private final static String PACK_NAME = "com.game.";
	private final static Map<Integer, MessageAssist> MESSAGE_ASSIST = new HashMap<>();
	/** handle的实例对象 */
	private final static Map<Class<?>, Object> HANDLE_OBJECT = new HashMap<>();
	
	public boolean isLogin(int mid) {
		switch (mid) {
		case MessageIds.reqLogin:
			return true;
		}
		return false;
	}
	
	/** 分发消息 */
	public void dispatch(Channel channel, Message message) {
		LinkStatus linkStatus = ContextUtil.getLinkStatu(channel);
		IGameDispatch gameDispatch =  ContextUtil.getGameDispath(channel);

		int mid = message.getMessageId();
		MessageAssist messageAssist = MESSAGE_ASSIST.get(mid);
		if (messageAssist == null) {
			LOGGER.error("channel ip {} , 发送消息 mid: {} ,不存在！！！！", new Object[] { ContextUtil.getRemoteIp(channel), mid });
			return;
		}

		if (linkStatus == LinkStatus.ANONYMOUS && !messageAssist.isAnonymous()) {
			LOGGER.error("channel ip {} , 发送消息 mid: {} ,不存在！！！！", new Object[] { ContextUtil.getRemoteIp(channel), mid });
			return;
		}
		
		try {
			Object handle = HANDLE_OBJECT.get(messageAssist.getHandleClass());
			if (handle == null) {
				LOGGER.error("找不到handk实例对象：" + messageAssist.getHandleClass().getName());
				return;
			}
			Object[] params = paramCovert.convertParam(channel, messageAssist.getParamClass(), message);

			MessageTask task = MessageTask.valueOf(gameDispatch.dispatchLine(), gameDispatch.dispatchMap(), handle,
					messageAssist.getMethod(), params, message);
			if (isLogin(message.getMessageId())) {
				HandleContext.getInst().addTask(task);
			} else {
				channel.eventLoop().execute(task);
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public static DispatchMessage getInst() {
		if (inst != null) {
			return inst;
		}
		synchronized (DispatchMessage.class) {
			if (inst != null) {
				return inst;
			}
			inst = new DispatchMessage();
			inst.init();
		}
		return inst;
	}
	
	public void init() {
		try {
			paramCovert = new DefaultParamCovert();
			
			List<Class<?>> classes = ClassUtil.scannerPack(PACK_NAME, new ClassFilter() {
				@Override
				public boolean filter(Class<?> clazz) {
					return clazz.getAnnotation(MessageHandler.class) != null;
				}
			});

			for (Class<?> clazz : classes) {
				boolean inst = false;
				Method[] methods = clazz.getDeclaredMethods();
				for (Method method : methods) {
					MessageMethod messageMethod = method.getAnnotation(MessageMethod.class);
					if (messageMethod != null) {
						inst = true;
						int mid = 0;
						boolean anonymous = messageMethod.anoymous();
						Class<?>[] paramTypes = method.getParameterTypes();
						for (Class<?> paramClass : paramTypes) {
							if (Message.class.isAssignableFrom(paramClass)) {
								MessageID messageID = paramClass.getAnnotation(MessageID.class);
								if (messageID != null) {
									mid = messageID.ID();
								} else {
									LOGGER.error("消息没有id：" + paramClass.getName());
								}
							}
						}
						if (mid <= 0) {
							LOGGER.error("方法没有消息,handleClass:{}, method:{} ", new Object[] { clazz.getName(), method.getName() });
							continue;
						}

						MESSAGE_ASSIST.put(mid, MessageAssist.valueOf(clazz, method, anonymous));
					}
				}

				if (inst) {
					Object object = clazz.newInstance();
					HANDLE_OBJECT.put(clazz, object);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

	}
	
}
