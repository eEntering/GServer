package com.game.player.message;

import com.game.message.Message;
import com.game.message.MessageIds;
import com.game.message.annotation.MessageID;

/**
 * @author caiweikai
 * @date 2019年3月18日
 */
@MessageID(ID = MessageIds.reqLogin)
public class ReqLoginMessage implements Message {
	
	private long userId;
	private String pass;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
}
