package com.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.server.HandlerServer;

public class ServerStart {
	
	private static Logger logger = LoggerFactory.getLogger(ServerStart.class);
	
	public static void main(String[] args) throws Exception {
		logger.error("-------启动服务器------");
		new HandlerServer().run();
		logger.error("------end-----");
	}
}
