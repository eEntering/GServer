package com.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.message.Message;
import com.game.player.message.ReqLoginMessage;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(ClientHandler.class);
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.error("建立连接");
		final ByteBuf buf = ctx.alloc().buffer(1024);
		ReqLoginMessage message = new ReqLoginMessage();
		Codec.encode(buf, message);
		
		ctx.executor().execute(new Runnable() {
			
			@Override
			public void run() {
				ctx.writeAndFlush(buf);
				logger.error("发送消息");
			}
		});
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf buf = (ByteBuf) msg;
		Message message = (Message) Codec.decode(buf);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
