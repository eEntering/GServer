package com.game.socket;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.socket.dispatch.IGameDispatch;
import com.game.utils.DispatchUtil;

/**
 * @author caiweikai
 * @date 2019年3月18日
 */
public abstract class BaseTask implements Runnable, IGameDispatch, Delayed {

	protected final static Logger LOGGER = LoggerFactory.getLogger(BaseTask.class);
	private int dispatchLine;
	private int dispatchMap;
	
	/** 时间单位：毫秒 */
	protected TimeUnit timeUnit = TimeUnit.MILLISECONDS;
	/** 延时单位时间填1000就是1秒 */
	protected long delayTime;
	protected long time;
	
	@Override
	public void run() {
		runTask();
	}
	
	public void runTask() {
		actionTask();
	}
	
	public abstract void actionTask();
	
	public BaseTask(int dispatchLine, int dispatchMap) {
		this(dispatchLine, dispatchMap, 0);
	}
	
	public BaseTask(int dispatchLine, int dispatchMap, int delayTime) {
		this.dispatchLine = dispatchLine;
		this.dispatchMap = dispatchMap;
		this.delayTime = delayTime;
		this.time = this.delayTime + System.currentTimeMillis();
	}
	
	
	@Override
	public long getDelay(TimeUnit unit) {
		return time - System.currentTimeMillis();
	}
	
	@Override
	public int compareTo(Delayed o) {
		BaseTask task = (BaseTask) o;
		return getDelay(timeUnit) > task.getDelay(timeUnit) ? 1 : getDelay(timeUnit) < task.getDelay(timeUnit) ? -1 : 0;
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
