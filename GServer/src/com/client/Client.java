package com.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Client {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 7979;
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(worker);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
//					.addLast("stringEncoder",new StringEncoder())
//					.addLast("StringDecoder", new StringDecoder())
					.addLast(new ClientHandler());
				}
			});
			
			ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			System.out.println("客户端消息出错了");
			System.out.println(e.getMessage());
			worker.shutdownGracefully();
		}
	}
}
