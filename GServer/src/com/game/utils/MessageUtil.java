package com.game.utils;

import com.game.message.Message;
import com.game.player.message.ResPlayerInfoMessage;
import com.game.player.struts.Player;
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
		for (Channel channel : SessionManager.getInst().allAnonymousSessions()) {
			SessionManager.getInst().write(channel, message);
		}
	}
	
	public static void notifyChannel(Channel channel, Message message) {
		SessionManager.getInst().write(channel, message);
	}

	public static void notifyPlayer(Player player, Message message) {
		SessionManager.getInst().write(player.getUserId(), message);
	}
}
