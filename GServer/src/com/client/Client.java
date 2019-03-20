package com.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	private static Logger logger = LoggerFactory.getLogger(Client.class);
	
	public static void main(String[] args) {
		final String host = "127.0.0.1";
		final int port = 7979;
		
		// 加载必要数据
		
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				EventLoopGroup worker = new NioEventLoopGroup();
				try {
					Bootstrap bootstrap = new Bootstrap();
					bootstrap.group(worker);
					bootstrap.channel(NioSocketChannel.class);
					bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
					bootstrap.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new ClientHandler());
						}
					});

					ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
					channelFuture.channel().closeFuture().sync();
				} catch (Exception e) {
					logger.error("客户端启动出错了");
					logger.error(e.getMessage());
					worker.shutdownGracefully();
				}
			}
		});
		
		thread.start();
	}
}
