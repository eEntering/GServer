package com.game.player.handler;

import com.game.message.annotation.MessageHandler;
import com.game.message.annotation.MessageMethod;
import com.game.player.bean.PlayerInfoBean;
import com.game.player.manager.PlayerManager;
import com.game.player.message.ReqLoginMessage;
import com.game.player.message.ReqPlayerInfoMessage;
import com.game.player.message.ResLoginMessage;
import com.game.player.message.ResPlayerInfoMessage;
import com.game.player.struts.Player;
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
		System.out.println(notice);
		PlayerManager.getInst().login(channel, req.getUserId(), req.getPass());
		ResLoginMessage message = new ResLoginMessage();
		message.setNotice(notice);
		PlayerInfoBean playerInfoBean = new PlayerInfoBean();
		playerInfoBean.setName(req.getPass());
		playerInfoBean.setPlayerId(req.getUserId());
		message.getPlyers().add(playerInfoBean);
		MessageUtil.notifyChannel(channel, message);
	}

	@MessageMethod
	public void reqPlayerInfo(long userId, ReqPlayerInfoMessage req) {
		Player player = PlayerManager.getInst().getOnlinePlayer(userId);
		if (player != null) {
//			PlayerManager.getInst().kickUser(player);
			ResPlayerInfoMessage message = new ResPlayerInfoMessage();
			MessageUtil.notifyPlayer(player, message);
		}
	}
}
