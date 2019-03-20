package com.game.socket.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  可命名线程组
 * @author caiweikai
 * @date 2019年3月19日
 */
public class NameThreadFactory implements ThreadFactory {

	/** 线程组 */
	private final ThreadGroup threadGroup;
	/** 线程组名字前缀 */
	private final String namePrefix;
	/** 线程编号 */
	private AtomicInteger number = new AtomicInteger(1);
	
	public NameThreadFactory(String groupName) {
		this.namePrefix = groupName + "-";
		threadGroup = new ThreadGroup(groupName);
	}
	
	@Override
	public Thread newThread(Runnable r) {
		return new Thread(threadGroup, r, getThreadName(), 0);
	}
	
	public Thread newDaemoThread(Runnable r, boolean daemo) {
		Thread thread = newThread(r);
		thread.setDaemon(daemo);
		return thread;
	}
	
	public Thread newDaemoThread(Runnable r) {
		Thread thread = newThread(r);
		thread.setDaemon(true);
		return thread;
	}
	
	private String getThreadName() {
		return new StringBuffer(namePrefix).append(number.getAndIncrement()).toString();
	}
}
