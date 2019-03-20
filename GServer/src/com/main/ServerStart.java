package com.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.server.HandlerServer;
import com.game.socket.HandleContext;
import com.game.utils.Codec;

public class ServerStart {
	
	private static Logger logger = LoggerFactory.getLogger(ServerStart.class);
	
	public static void main(String[] args) throws Exception {
		logger.error("-------启动服务器------");
		Codec.init();
		HandleContext.getInst();
		
		new HandlerServer().run();
		logger.error("------end-----");
	}
}
