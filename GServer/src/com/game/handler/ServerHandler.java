package com.game.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.game.message.Message;
import com.game.session.SessionManager;
import com.game.socket.DispatchMessage;
import com.game.utils.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		logger.error("channel active : [{}]  timestamp : [{}]", new Object[] { ctx, System.currentTimeMillis() });
		SessionManager.getInst().putAnonymous(ctx.channel());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 解码
		ByteBuf buf = (ByteBuf) msg;
		Message message = (Message) Codec.decode(buf);
		if (message == null) {
			logger.error("消息解码为空");
			return;
		}
		
		// 任务分发出去执行业务逻辑
		DispatchMessage.getInst().dispatch(ctx.channel(), message);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SessionManager.getInst().removeAnonymous(ctx.channel().id().asLongText());
		logger.error("channel Inactive : [{}]  timestamp : [{}]", new Object[] { ctx, System.currentTimeMillis() });
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("出现问题！！！！！");
	}
	
	@Override
	public boolean isSharable() {
		return true;
	}
}
