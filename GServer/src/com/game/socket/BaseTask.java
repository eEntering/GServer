package com.game.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.socket.dispatch.IGameDispatch;
import com.game.utils.DispatchUtil;

/**
 * @author caiweikai
 * @date 2019年3月18日
 */
public abstract class BaseTask implements Runnable, IGameDispatch {

	protected final static Logger LOGGER = LoggerFactory.getLogger(BaseTask.class);
	private int dispatchLine;
	private int dispatchMap;
	
	@Override
	public void run() {
		runTask();
	}
	
	public void runTask() {
		actionTask();
	}
	
	public abstract void actionTask();
	
	public BaseTask(int dispatchLine, int dispatchMap) {
		this.dispatchLine = dispatchLine;
		this.dispatchMap = dispatchMap;
	}
	
	public int getDispatchId() {
		return DispatchUtil.getDispatchId(dispatchLine, dispatchMap);
	}
	@Override
	public int dispatchLine() {
		return dispatchLine;
	}
	
	@Override
	public int dispatchMap() {
		return dispatchMap;
	}
}
