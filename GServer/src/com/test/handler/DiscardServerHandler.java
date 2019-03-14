package com.test.handler;

import com.test.util.MessageUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		try {
//			ByteBuf buf = (ByteBuf) msg;
//			while (buf.isReadable()) {
//				System.out.println(buf.readByte());
//				System.out.println((char)bduf.readByte());
//				System.out.flush();
//			}
//			ctx.write(msg); // write之后会自动释放引用计数对象
//			ctx.flush();
//		ctx.writeAndFlush(msg);
//			System.out.println(msg.toString());
//		} finally {
//			MessageUtils.release(msg);
//		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		final ByteBuf buf = ctx.alloc().buffer(4);
		buf.writeInt(1000);
		final ChannelFuture f = ctx.writeAndFlush(buf);
		f.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				assert f==future;
				ctx.close();
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
