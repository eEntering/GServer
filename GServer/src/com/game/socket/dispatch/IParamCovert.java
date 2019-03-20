package com.game.socket.dispatch;

import com.game.message.Message;

import io.netty.channel.Channel;

/**
 * @author caiweikai
 * @date 2019年3月20日
 */
public interface IParamCovert {

	Object[] convertParam(Channel channel, Class<?>[] paramClass, Message message);
}
