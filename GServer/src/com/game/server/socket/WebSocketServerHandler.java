package com.game.server.socket;

import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.handler.ServerHandler;
import com.game.message.Message;
import com.game.player.bean.PlayerInfoBean;
import com.game.player.message.ResLoginMessage;
import com.game.session.SessionManager;
import com.game.socket.DispatchMessage;
import com.game.socket.constant.ChannelCloseType;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * @author caiweikai
 * @date 2019年4月2日
 */
public class WebSocketServerHandler extends ChannelInboundHandlerAdapter {
	
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
		// 传输json
//		ResLoginMessage message = new ResLoginMessage();
//		message.setNotice("123");
//		PlayerInfoBean playerInfoBean = new PlayerInfoBean();
//		playerInfoBean.setName("护法u");
//		playerInfoBean.setPlayerId(15151151L);
//		message.getPlyers().add(playerInfoBean);
//		String json = JSON.toJSON(message).toString();
//		System.out.println(json);
//		ctx.channel().writeAndFlush(new TextWebSocketFrame(json));
		try {
			WebShocketFrameHandle.handleMsg(ctx.channel(), msg);
		} catch (Exception e) {
			logger.error("处理WebSocket消息出错！！！");
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
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public boolean isSharable() {
		return true;
	}
}
