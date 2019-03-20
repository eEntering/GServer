package com.game.utils;

import com.game.socket.dispatch.IGameDispatch;

import io.netty.util.AttributeKey;

/**
 * @author caiweikai
 * @date 2019年3月19日
 */
public interface AttributeType {

	/** 连接状态类型 */
	AttributeKey<LinkStatus> LINK_STATUS_KEY = AttributeKey.valueOf("LINK_STATUS_KEY");
	
	/** 客户端id */
	AttributeKey<String> CLIENT_IP_KEY = AttributeKey.valueOf("CLIENT_IP_KEY");

	/** 线路分发器 */
	AttributeKey<IGameDispatch> GAME_DISPATCH_KEY = AttributeKey.valueOf("GAME_DISPATCH_KEY");
	
	/** 玩家id */
	AttributeKey<Long> PLAYER_ID_KEY = AttributeKey.valueOf("PLAYER_ID_KEY");
	
}
