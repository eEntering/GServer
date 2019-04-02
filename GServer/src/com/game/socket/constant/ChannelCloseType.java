package com.game.socket.constant;

/**
 * @author caiweikai
 * @date 2019年3月25日
 */
public enum ChannelCloseType {
	
	// 踢下线
	KICK(1),
	// 断线
	INACTIVITE(2),
	// 顶号
	REPLACE(3),
	;
	
	private int type;
	
	private ChannelCloseType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
