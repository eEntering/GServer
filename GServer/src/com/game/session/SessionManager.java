package com.game.session;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.message.Message;
import com.game.player.manager.PlayerManager;
import com.game.player.message.ResPlayerInfoMessage;
import com.game.player.struts.Player;
import com.game.socket.constant.ChannelCloseType;
import com.game.utils.Codec;
import com.game.utils.ContextUtil;
import com.game.utils.LinkStatus;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author caiweikai
 * @date 2019年3月19日
 */
public class SessionManager {

	private static Logger logger = LoggerFactory.getLogger(SessionManager.class);
	private static SessionManager inst;
	/** 匿名用户连接 */
	private static final ConcurrentHashMap<String, Channel> ANONYMOUS_SESSION_MAP = new ConcurrentHashMap<>();
	/** 匿名连接数 */
	public static final AtomicInteger ANONYMOUS_SESSION_COUNT = new AtomicInteger();
	/** 登陆用户连接 */
	private static final ConcurrentHashMap<Long, Channel> USER_SESSION_MAP = new ConcurrentHashMap<>();
	/** 登陆连接数 */
	public static final AtomicInteger USER_SESSION_COUNT = new AtomicInteger();
	
	public static SessionManager getInst() {
		if (inst != null) {
			return inst;
		}
		synchronized (SessionManager.class) {
			if (inst != null) {
				return inst;
			}
			inst = new SessionManager();
		}
		return inst;
	}
	
	public void putAnonymous(Channel channel) {
		if (channel != null) {
			ANONYMOUS_SESSION_MAP.put(channel.id().asLongText(), channel);
			ANONYMOUS_SESSION_COUNT.incrementAndGet();
		}
	}
	
	public void removeAnonymous(String channelId, int type) {
		if (channelId != null) {
			Channel channel = ANONYMOUS_SESSION_MAP.remove(channelId);
			if (channel != null) {
				ANONYMOUS_SESSION_COUNT.decrementAndGet();
				closeChannel(channel, type);
			}
		}
	}
	
	public void removeChannel(Channel channel, int type) {
		LinkStatus linkStatus = ContextUtil.getLinkStatu(channel);
		if (linkStatus == LinkStatus.LOGIN) {
			removeUser(channel, type);
		} else {
			removeAnonymous(channel.id().asLongText(), type);
		}
	}
	
	public Channel putUser(Channel channel, long userId) {
		if (channel != null) {
			String channelId = channel.id().asLongText();
			if (ANONYMOUS_SESSION_MAP.containsKey(channelId)) {
				ANONYMOUS_SESSION_MAP.remove(channelId);
				ANONYMOUS_SESSION_COUNT.decrementAndGet();
			}
			Channel oldChannel = USER_SESSION_MAP.get(userId);
			if (oldChannel != null && oldChannel != channel) {
				closeChannel(oldChannel, ChannelCloseType.REPLACE.getType());
				USER_SESSION_COUNT.decrementAndGet();
			}

			USER_SESSION_MAP.put(userId, channel);
			USER_SESSION_COUNT.incrementAndGet();
		}
		return channel;
	}

	/** 移除用户连接 */
	public void removeUser(long userId, int type) {
		Channel channel = USER_SESSION_MAP.get(userId);
		if (channel != null) {
			USER_SESSION_MAP.remove(userId);
			USER_SESSION_COUNT.decrementAndGet();
			closeChannel(channel, type);
		}
	}
	
	/** 移除用户连接 */
	public void removeUser(Channel channel, int type) {
		long userId = ContextUtil.getUserId(channel);
		Channel currChannel = USER_SESSION_MAP.get(userId);
		if (currChannel != null && channel != null && currChannel == channel) {
			removeUser(userId, type);
		}
	}
	
	public Collection<Channel> allUserSessions(){
		return USER_SESSION_MAP.values();
	}
	
	public Collection<Channel> allAnonymousSessions() {
		return ANONYMOUS_SESSION_MAP.values();
	}
	
	public int getALlSessionCount() {
		return ANONYMOUS_SESSION_COUNT.get() + USER_SESSION_COUNT.get();
	}
	
	public void write2Anonymous(Message message) {
		for (Channel channel : ANONYMOUS_SESSION_MAP.values()) {
			write(channel, message);
		}
	}
	
	public void write(Channel channel, Message message) {
		if (channel != null && channel.isActive() && channel.isOpen()) {
			ByteBuf buf = channel.alloc().buffer();
			Codec.encode(buf, message);
			// 直接发送二进制消息
			// channel.writeAndFlush(buf);
			// 转成websocket消息发送
			BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(buf);
			channel.writeAndFlush(binaryWebSocketFrame);
		}
	}

	public void write(long userId, Message message) {
		Channel channel = USER_SESSION_MAP.get(userId);
		if (channel != null) {
			write(channel, message);
		}
	}
	
	private void closeChannel(Channel channel, int code) {
		String playerName = "";
		long userId = ContextUtil.getUserId(channel);
		Player player = PlayerManager.getInst().getPlayer(userId);
		if (player != null) {
			playerName = player.getName();
		}
		final String name = playerName;
		if (channel != null && channel.isActive() && channel.isOpen()) {
			logger.error("start to close channel [{}], code [{}], timestamp [{}], playerName [{}]",
					new Object[] { channel, code, System.currentTimeMillis(), playerName });
			ChannelFuture future = channel.close();
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					logger.error("complete close channel [{}], code [{}], timestamp [{}], playerName [{}]",
							new Object[] { channel, code, System.currentTimeMillis(), name });
				}
			});
		}
	}
	
}
