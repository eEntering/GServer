package com.game.player.struts;

import com.game.socket.dispatch.IGameDispatch;

/**
 * @author caiweikai
 * @date 2019年3月25日
 */
public class Player implements IGameDispatch {

	private long userId;
	private String name;
	private int line;
	private int map;
	
	@Override
	public int dispatchLine() {
		return line;
	}

	@Override
	public int dispatchMap() {
		return map;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}

}
