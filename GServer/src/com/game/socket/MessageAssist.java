package com.game.socket;

import java.lang.reflect.Method;

/**
 *  消息辅助类
 * @author caiweikai
 * @date 2019年3月20日
 */
public class MessageAssist {
	
	/** handle Class */
	private Class<?> handleClass;
	/** method */
	private Method method;
	/** 是否可匿名访问 */
	private boolean anonymous;
	/** 参数类型 */
	private Class<?>[] paramClass;
	
	public static MessageAssist valueOf(Class<?> handleClass, Method method, boolean anonymous) {
		MessageAssist messageAssist = new MessageAssist();
		messageAssist.setHandleClass(handleClass);
		messageAssist.setMethod(method);
		messageAssist.setAnonymous(anonymous);
		messageAssist.setParamClass(method.getParameterTypes());
		return messageAssist;
	}
	public Class<?> getHandleClass() {
		return handleClass;
	}
	public void setHandleClass(Class<?> handleClass) {
		this.handleClass = handleClass;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public boolean isAnonymous() {
		return anonymous;
	}
	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}
	public Class<?>[] getParamClass() {
		return paramClass;
	}
	public void setParamClass(Class<?>[] paramClass) {
		this.paramClass = paramClass;
	}
}
