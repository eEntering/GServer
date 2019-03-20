package com.game.session;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.game.message.Message;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * @author caiweikai
 * @date 2019年3月19日
 */
public class SessionManager {

	private static SessionManager inst;
	/** 匿名用户连接 */
	private static final ConcurrentHashMap<String, Channel> ANONYMOUS_SESSION_MAP = new ConcurrentHashMap<>();
	/** 登陆用户连接 */
	private static final ConcurrentHashMap<Long, Channel> USER_SESSION_MAP = new ConcurrentHashMap<>();
	
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
		}
	}
	
	public void removeAnonymous(String channelId) {
		if (channelId != null) {
			ANONYMOUS_SESSION_MAP.remove(channelId);
		}
	}
	
	public void putUser(Channel channel, long userId) {
		if (channel != null) {
			String channelId = channel.id().asLongText();
			if (ANONYMOUS_SESSION_MAP.containsKey(channelId)) {
				ANONYMOUS_SESSION_MAP.remove(channelId);
			}
			Channel oldChannel = USER_SESSION_MAP.get(userId);
			if (oldChannel != null && oldChannel != channel) {
				oldChannel.close();
			}
			
			USER_SESSION_MAP.put(userId, channel);
		}
	}
	
	public Collection<Channel> allUserSessions(){
		return USER_SESSION_MAP.values();
	}
	
	public Collection<Channel> allAnonymousSessions() {
		return ANONYMOUS_SESSION_MAP.values();
	}
	
	public void write2Anonymous(Message message) {
		for (Channel channel : ANONYMOUS_SESSION_MAP.values()) {
			write(channel, message);
		}
	}
	
	public void write(Channel channel, Message message) {
		if (channel.isActive()) {
			ByteBuf buf = channel.alloc().buffer();
			Codec.encode(buf, message);
			channel.writeAndFlush(buf);
		}
	}
}
