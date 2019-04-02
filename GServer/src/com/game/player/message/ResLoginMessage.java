package com.game.player.message;

import java.util.ArrayList;
import java.util.List;

import com.game.message.Message;
import com.game.message.MessageIds;
import com.game.message.annotation.MessageID;
import com.game.player.bean.PlayerInfoBean;

@MessageID(ID = MessageIds.resLgin)
public class ResLoginMessage implements Message {
	
	private String notice;
	private List<PlayerInfoBean> plyers = new ArrayList<>();

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public List<PlayerInfoBean> getPlyers() {
		return plyers;
	}

	public void setPlyers(List<PlayerInfoBean> plyers) {
		this.plyers = plyers;
	}
}
