package com.main;

import com.alibaba.fastjson.JSON;
import com.game.player.bean.PlayerInfoBean;
import com.game.player.message.ReqLoginMessage;
import com.game.player.message.ResLoginMessage;
import com.game.player.message.ResPlayerInfoMessage;

/**
 * @author caiweikai
 * @date 2019年3月21日
 */
public class TestMain {

	public static void main(String[] args) {
		
		
		ReqLoginMessage message = new ReqLoginMessage();
		message.setPass("222222");
		message.setUserId(5555);
		System.out.println(JSON.toJSON(message));
		
	}
	
}
