package com.client;

import com.game.codec.ICodec;
import com.game.message.TestMessage;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ICodec codec = Codec.getCodec(Object.class);
		ByteBuf buf = (ByteBuf) msg;
		TestMessage message = (TestMessage) codec.read(buf, TestMessage.class);
		System.out.println(1);
		try {
			ctx.close();
		} catch (Exception e) {
			buf.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
