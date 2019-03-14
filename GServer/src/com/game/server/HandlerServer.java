package com.game.server;

import com.game.handler.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HandlerServer {

	private final int port = 7979;

	public void run() throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup(); // 接收连接
		EventLoopGroup worker = new NioEventLoopGroup(); // 处理连接请求业务

		try {
			ServerBootstrap bootstrap = new ServerBootstrap(); // 这是一个设置服务器的帮助类
			bootstrap.group(boss, worker)
			
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline()
							.addLast("server",new ServerHandler());
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = bootstrap.bind(port).sync();

			future.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
