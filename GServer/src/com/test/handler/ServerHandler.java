package com.test.handler;

import java.nio.charset.Charset;

import com.cwk.codec.ICodec;
import com.cwk.utils.Codec;
import com.test.mess.TestMessage;

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
//		buf.writeCharSequence((CharSequence)"22222222", Charset.forName("UTF-8"));
//		buf.writeInt(1);
//		buf.writeLong(2);
//		
//		buf.
//		buf.readInt();
		
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
