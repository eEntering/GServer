package com.game.player.message;

import com.game.message.Message;
import com.game.message.MessageIds;
import com.game.message.annotation.MessageID;

@MessageID(ID = MessageIds.resLgin)
public class ResLoginMessage implements Message {
	
	private String notice;

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
}
