package com.client;

import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.Date;

import org.ietf.jgss.MessageProp;

import com.cwk.codec.ICodec;
import com.cwk.utils.Codec;
import com.test.mess.MessageDecoder;
import com.test.mess.TestMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(22222);
//		TestMessage message = (TestMessage) msg;
		System.out.println(111);
		
		ICodec codec = Codec.getCodec(Object.class);
		ByteBuf buf = (ByteBuf) msg;
		TestMessage message = (TestMessage) codec.read(buf, TestMessage.class);
		System.out.println(1);
		try {
			long time = buf.readInt() * 1000L;
			System.out.println(new Date(time));
			ctx.close();
		} catch (Exception e) {
//			buf.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
