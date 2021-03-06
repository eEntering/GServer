package com.game.handler;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.game.message.Message;
import com.game.player.message.ResLoginMessage;
import com.game.session.SessionManager;
import com.game.socket.DispatchMessage;
import com.game.socket.constant.ChannelCloseType;
import com.game.utils.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
	private AtomicInteger atomic = new AtomicInteger();
	private long curr = System.currentTimeMillis();
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SessionManager.getInst().putAnonymous(ctx.channel());
		logger.error("channel active : [{}]  timestamp : [{}], 连接数 : {}",
				new Object[] { ctx, System.currentTimeMillis(), SessionManager.getInst().getALlSessionCount() });
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
		int a = atomic.incrementAndGet();
		if (a % 100 == 0) {
			long now = System.currentTimeMillis();
			System.out.println("===============" + a + "============" + (now - curr));
			curr = now;
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SessionManager.getInst().removeChannel(ctx.channel(), ChannelCloseType.INACTIVITE.getType());
		logger.error("channel Inactive : [{}]  timestamp : [{}], 连接数 : {}",
				new Object[] { ctx, System.currentTimeMillis(), SessionManager.getInst().getALlSessionCount() });
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		System.out.println("出现问题！！！！！");
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public boolean isSharable() {
		return true;
	}
}
