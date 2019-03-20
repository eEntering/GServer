package com.game.utils;

import java.net.InetSocketAddress;

import com.game.socket.dispatch.IGameDispatch;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 *  上下文帮助类
 * @author caiweikai
 * @date 2019年3月19日
 */
public class ContextUtil {
	
	public static String getRemoteIp(Channel channel) {
		if (channel == null) {
			return StringUtils.EMPTY;
		}
		String clientIp = getAttribute(channel, AttributeType.CLIENT_IP_KEY, String.class);
		if (!StringUtils.isEmpty(clientIp)) {
			return clientIp;
		}
		try {
			clientIp = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
			if (StringUtils.isEmpty(clientIp)) {
				clientIp = ((InetSocketAddress) channel.localAddress()).getAddress().getHostAddress();
			}
			setAttribute(channel, AttributeType.CLIENT_IP_KEY, clientIp);
		} catch (Exception e) {
			clientIp = StringUtils.EMPTY;
		}

		return clientIp;
	}
	
	/** 获取连接状态 */
	public static LinkStatus getLinkStatu(Channel channel) {
		LinkStatus linkStatus = getAttribute(channel, AttributeType.LINK_STATUS_KEY, LinkStatus.class);
		if (linkStatus == null) {
			setAttribute(channel, AttributeType.LINK_STATUS_KEY, LinkStatus.ANONYMOUS);
			linkStatus = getAttribute(channel, AttributeType.LINK_STATUS_KEY, LinkStatus.class);
		}
		return linkStatus;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getAttribute(Channel channel, AttributeKey<?> attributeKey, Class<T> clazz) {
		return (T) channel.attr(attributeKey).get();
	}

	public static <T> void setAttribute(Channel channel, AttributeKey<T> attributeKey, T value) {
		channel.attr(attributeKey).set(value);
	}

	/** 获取分发器 */
	public static IGameDispatch getGameDispath(Channel channel) {
		IGameDispatch gameDispatch = getAttribute(channel, AttributeType.GAME_DISPATCH_KEY, IGameDispatch.class);
		if (gameDispatch == null) {
			setAttribute(channel, AttributeType.GAME_DISPATCH_KEY, new IGameDispatch.AnonymousDispatch());
			gameDispatch = getAttribute(channel, AttributeType.GAME_DISPATCH_KEY, IGameDispatch.class);
		}
		return gameDispatch;
	}

	public static long getPlayerId(Channel channel) {
		if (channel == null) {
			return 0;
		}
		Long playerId = getAttribute(channel, AttributeType.PLAYER_ID_KEY, Long.class);
		return playerId == null ? 0 : playerId;
	}
}
