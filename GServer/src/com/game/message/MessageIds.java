package com.game.message;

/**
 * 消息id
 * @author caiweikai
 * @date 2019年3月18日
 */
public interface MessageIds {
	/** 請求登陸 */
	int reqLogin = 10000001;
	/** 請求玩家信息 */
	int reqPlayerInfo = 10000002;
	
	
	/** 返回登陆 */
	int resLgin = 10000101;
	/** 返回玩家信息 */
	int resPlayerInfo = 10000102;
	
}
