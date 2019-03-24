package com.game.player.handler;

import com.game.message.annotation.MessageHandler;
import com.game.message.annotation.MessageMethod;
import com.game.player.message.ReqLoginMessage;
import com.game.player.message.ResLoginMessage;
import com.game.utils.MessageUtil;
import io.netty.channel.Channel;

/**
 * @author caiweikai
 * @date 2019年3月18日
 */
@MessageHandler
public class PlayerMessageHandler {
	
	/** 请求登陆 */
	@MessageMethod(anoymous = true)
	public void reqLogin(Channel channel, ReqLoginMessage req) {
		String notice = req.getUserId() + "(" + req.getPass() + "), 你好！";
//		System.out.println(notice);

		ResLoginMessage message = new ResLoginMessage();
		message.setNotice(notice);
		MessageUtil.notifyChannel(channel, message);
	}
}
