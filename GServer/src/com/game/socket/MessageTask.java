package com.game.socket;

import java.lang.reflect.Method;
import java.util.concurrent.Delayed;

import com.game.message.Message;
import com.game.message.MessageIds;

/**
 * @author caiweikai
 * @date 2019年3月20日
 */
public class MessageTask extends BaseTask {
	
	/** 对象handle */
	private Object handle;
	private Method method;
	private Object[] params;
	private transient Message message;

	public static MessageTask valueOf(int dispatchLine, int dispatchMap, Object handle, Method method,
			Object[] params, Message message) {
		return new MessageTask(dispatchLine, dispatchMap, handle, method, params, message);
	}

	public MessageTask(int dispatchLine, int dispatchMap, Object handle, Method method, Object[] params, Message message) {
		super(dispatchLine, dispatchMap);
		this.handle = handle;
		this.method = method;
		this.params = params;
		this.message = message;
	}

	@Override
	public void actionTask() {
		try {
			method.invoke(handle, params);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public boolean isLogin() {
		if (message != null) {
			int mid = message.getMessageId();
			switch (mid) {
			case MessageIds.reqLogin:
				return true;
			}
		}
		return false;
	}
}
