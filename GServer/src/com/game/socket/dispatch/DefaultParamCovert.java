package com.game.socket.dispatch;

import com.game.message.Message;
import com.game.utils.ContextUtil;

import io.netty.channel.Channel;

/**
 *  默认参数转换器
 * @author caiweikai
 * @date 2019年3月20日
 */
public class DefaultParamCovert implements IParamCovert {

	@Override
	public Object[] convertParam(Channel channel, Class<?>[] paramClass, Message message) {
		Object[] params = new Object[paramClass == null ? 0 : paramClass.length];

		for (int i = 0; i < params.length; i++) {
			Class<?> paramClazz = paramClass[i];
			if (Channel.class.isAssignableFrom(paramClazz)) {
				params[i] = channel;
			} else if (long.class.isAssignableFrom(paramClazz)) {
				params[i] = ContextUtil.getUserId(channel);
			} else if (Long.class.isAssignableFrom(paramClazz)) {
				params[i] = ContextUtil.getUserId(channel);
			} else if (Message.class.isAssignableFrom(paramClazz)) {
				params[i] = message;
			}
//			else if (Message.class.isAssignableFrom(paramClazz)) {
//				params[i] = message;
//			}
		}

		return params;
	}
}
