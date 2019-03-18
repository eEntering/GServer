package com.game.handler;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.codec.ICodec;
import com.game.message.TestMessage;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		logger.error("建立连接");
//		final ByteBuf time = ctx.alloc().buffer(4);
//		time.writeInt(1);
//		ctx.executor().execute(new Runnable() {
//			@Override
//			public void run() {
//				ctx.writeAndFlush(time);
//				logger.error("发送消息");
//			}
//		});
//		ByteBuf buf = ctx.alloc().buffer(1024);
		
//		TestMessage message = new TestMessage();
//		message.setString("1234444");
//		message.setI(58);
//		ICodec codec = Codec.getCodec(Object.class);
//		codec.write(buf, message, message.getClass(),null);
		
//		final ChannelFuture channelFuture = ctx.writeAndFlush(buf);
//		channelFuture.addListener(new ChannelFutureListener() {
//			@Override
//			public void operationComplete(ChannelFuture future) {
//				assert channelFuture == future;
//				System.out.println("消息发送了");
//				ctx.close();
//			}
//		});
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 解码
		// 任务分发出去执行业务逻辑
		ByteBuf buf = (ByteBuf) msg;
		int i = buf.readInt();
		logger.error("解码消息:" + i);
		buf.writeInt(i+1);
		ctx.executor().execute(new Runnable() {
			
			@Override
			public void run() {
				ctx.writeAndFlush(buf);
				logger.error("发送消息");
			}
		});
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		System.out.println("出现问题！！！！！");
		System.out.println(cause.getMessage());
	}
	
	@Override
	public boolean isSharable() {
		return true;
	}
}
