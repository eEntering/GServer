package com.game.utils;

import com.game.message.Message;
import com.game.session.SessionManager;

import io.netty.channel.Channel;

/**
 * @author caiweikai
 * @date 2019年3月19日
 */
public class MessageUtil {
	
	public static void notifyAllPlayer() {
		
	}
	
	public static void notifyAllAnonymous(Message message) {
//		SessionManager.getInst().
		for(Channel channel : SessionManager.getInst().allAnonymousSessions()) {
//			channel.writeAndFlush(msg)
		}
	}
}
