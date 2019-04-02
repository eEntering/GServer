package com.game.player.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.player.struts.Player;
import com.game.session.SessionManager;
import com.game.socket.constant.ChannelCloseType;
import com.game.socket.dispatch.IGameDispatch;
import com.game.utils.AttributeType;
import com.game.utils.ContextUtil;
import com.game.utils.LinkStatus;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @author caiweikai
 * @date 2019年3月25日
 */
public class PlayerManager {
	
	private static Logger logger = LoggerFactory.getLogger(PlayerManager.class);
	private ConcurrentHashMap<Long, Player> playerMap = new ConcurrentHashMap<>();
	private CopyOnWriteArraySet<Long> onlinesPlayer = new CopyOnWriteArraySet<>();
	private AtomicInteger onlineCount = new AtomicInteger(0);
	private AtomicLong line = new AtomicLong(0);
	private AtomicInteger map = new AtomicInteger(0);

	private static PlayerManager inst;

	public static PlayerManager getInst() {
		if (inst != null) {
			return inst;
		}
		synchronized (PlayerManager.class) {
			if (inst != null) {
				return inst;
			}
			inst = new PlayerManager();
		}
		return inst;
	}
	
	public void kickUser(Player player) {
		SessionManager.getInst().removeUser(player.getUserId(), ChannelCloseType.KICK.getType());
		removeOnlinePlayer(player.getUserId());
		logger.error("玩家被踢下线：{},{} ", new Object[] { player.getUserId(), player.getName() });
	}
	
	public void bindChannel(Channel channel, Player player) {
		SessionManager.getInst().putUser(channel, player.getUserId());
		
		ContextUtil.setAttribute(channel, AttributeType.USER_ID_KEY, player.getUserId());
		ContextUtil.setAttribute(channel, AttributeType.LINK_STATUS_KEY, LinkStatus.LOGIN);
		ContextUtil.setAttribute(channel, AttributeType.GAME_DISPATCH_KEY, player);
	}
	
	public void login(Channel channel, long userId, String password) {
		// 校验
		Player player = new Player();
		player.setUserId(userId);
		player.setLine(getLine());
		player.setMap(getMap());
		
		putOnlinePlayer(player);
		bindChannel(channel, player);
		
	}
	
	public Player getPlayer(long userId) {
		Player player = playerMap.get(userId);
		if (player != null) {
			return player;
		}
		
		// 数据库查
		
		return null;
	}
	
	public Player getOnlinePlayer(long userId) {
		if (!onlinesPlayer.contains(userId)) {
			return null;
		}
		Player player = playerMap.get(userId);
		if (player != null) {
			return player;
		}
		
		return null;
	}
	
	public void putOnlinePlayer(Player player) {
		playerMap.put(player.getUserId(), player);
		onlinesPlayer.add(player.getUserId());
		onlineCount.incrementAndGet();
	}
	
	private void removeOnlinePlayer(long userId) {
		playerMap.remove(userId);
		if (onlinesPlayer.remove(userId)) {
			onlineCount.decrementAndGet();
		}
	}
	
	private int getLine() {
		long tmp = line.incrementAndGet();
		if (tmp >= Integer.MAX_VALUE) {
			line.set(1);
			map.incrementAndGet();
			tmp = line.get();
		}
		return (int) tmp;
	}
	
	private int getMap() {
		return map.get();
	}
}
