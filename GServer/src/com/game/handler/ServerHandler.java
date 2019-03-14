package com.game.handler;

import java.nio.charset.Charset;

import com.game.codec.ICodec;
import com.game.message.TestMessage;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		final ByteBuf time = ctx.alloc().buffer(4);
		
		ByteBuf buf = ctx.alloc().buffer(1024);
		
		TestMessage message = new TestMessage();
		message.setString("1234444");
		message.setI(58);
		ICodec codec = Codec.getCodec(Object.class);
		codec.write(buf, message, message.getClass());
		
		final ChannelFuture channelFuture = ctx.writeAndFlush(buf);
		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				assert channelFuture == future;
				System.out.println("消息发送了");
				ctx.close();
			}
		});
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		System.out.println("出现问题！！！！！");
		System.out.println(cause.getMessage());
	}
}
