package com.client;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.message.Message;
import com.game.player.message.ReqLoginMessage;
import com.game.player.message.ReqPlayerInfoMessage;
import com.game.player.message.ResLoginMessage;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GenericFutureListener;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(ClientHandler.class);
	private static Random random = new Random();
	private static String pass = UUID.randomUUID().toString();
//	private static long user = random.nextLong();
	private static long user = 1;
	private AtomicInteger atomic = new AtomicInteger();
	private long curr = System.currentTimeMillis();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.error("建立连接");
		int i=1;
//		while (true) {
			final ByteBuf buf = ctx.alloc().buffer(1024);
			ReqLoginMessage message = new ReqLoginMessage();
			message.setUserId(user);
			message.setPass(pass);
			Codec.encode(buf, message);
//			System.out.println(i);
			ChannelFuture future = ctx.writeAndFlush(buf);
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
//					ctx.close();
//					ReferenceCountUtil.release(buf);
				}
			});
			i++;
			if (i==6533340) {
				System.out.println(1);
			}
//		}
		
//		ctx.executor().execute(new Runnable() {
//			
//			@Override
//			public void run() {
//				ctx.writeAndFlush(buf);
//				logger.error("发送消息");
//			}
//		});
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf buf = (ByteBuf) msg;
		Message message = (Message) Codec.decode(buf);
		
		if (message instanceof ResLoginMessage) {
//			System.out.println("收到返回消息：： "+((ResLoginMessage) message).getNotice());
		}
		final ByteBuf sbuf = ctx.alloc().buffer(1024);
		ReqPlayerInfoMessage smessage = new ReqPlayerInfoMessage();
//		smessage.setUserId(user);
//		smessage.setPass(pass);
		Codec.encode(sbuf, smessage);
//		ChannelFuture future = ctx.writeAndFlush(sbuf);
		
		int a = atomic.incrementAndGet();
		if (a % 100 == 0) {
			long now = System.currentTimeMillis();
			System.out.println("===============" + a + "============" + (now - curr));
			curr = now;
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.error("断开连接");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
