package com.game.server;

import com.game.handler.ServerHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * TCP连接使用的
 * @author caiweikai
 * @date 2019年4月2日
 */
public class ServerChildHandler extends ChannelInitializer<SocketChannel> {

	private final static ServerHandler SERVER_HANDLER = new ServerHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("server", SERVER_HANDLER);
	}

}
