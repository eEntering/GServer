package com.game.socket;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.socket.thread.NameThreadFactory;

/**
 *  消息处理上下文
 * @author caiweikai
 * @date 2019年3月19日
 */
public class HandleContext {

	private static Logger logger = LoggerFactory.getLogger(HandleContext.class);
	private static HandleContext inst;
	/** 处理器数量 */
	private final int CORE = Runtime.getRuntime().availableProcessors();
	/** 工作线程数量 */
	private final int CORE_SIZE = CORE * 2;
	/** 未登录工作线程数量 */
	private final int NO_LOGON_CORE_SIZE =  2;
	/** 登陆线程 */
	private ExecutorService LOGIN_EXECUTOR = Executors.newFixedThreadPool(2, new NameThreadFactory("login"));
	private WorkerThread[] loginWorker = new WorkerThread[CORE_SIZE];
	private WorkerThread[] noLoginWorker = new WorkerThread[NO_LOGON_CORE_SIZE];
	private AtomicLong noLoginCount = new AtomicLong(0);

	public static HandleContext getInst() {
		if (inst != null) {
			return inst;
		}
		synchronized (HandleContext.class) {
			if (inst != null) {
				return inst;
			}
			inst = new HandleContext();
			inst.init();
		}
		return inst;
	}
	
	private void init() {
		for (int i = 0; i < loginWorker.length; i++) {
			WorkerThread thread = new WorkerThread(i);
			loginWorker[i] = thread;
			new NameThreadFactory("loginWorker").newDaemoThread(thread).start();
		}
		
		for (int i = 0; i < noLoginWorker.length; i++) {
			WorkerThread thread = new WorkerThread(i);
			noLoginWorker[i] = thread;
			new NameThreadFactory("noLoginWorker").newDaemoThread(thread).start();
		}
	}
	
	/** 工作线程 */
	private class WorkerThread implements Runnable {

		/** 阻塞队列,take数据时会一直阻塞到取到数据 */
		private DelayQueue<BaseTask> taskQuene = new DelayQueue<>();
		/** 线程索引 */
		private int threadIndex;
		/** TPS */
		private int tps;
		/** 处理任务数 */
		private AtomicLong atomicLong = new AtomicLong();
		
		
		public WorkerThread(int index) {
			this.threadIndex = index;
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					// 从队首取元素，如果队列为空，则等待
					BaseTask task = taskQuene.take();
					if (task != null) {
						task.runTask();
					}
					
					// 定时任务需要继续添加进队列
				}
			} catch (Exception e) {
				logger.error("工作者线程执行异常：" + this.threadIndex);
				logger.error(e.getMessage());
			}
		}

		public void addTask(BaseTask task) {
			taskQuene.add(task);
		}

	}
	
	public void addTask(BaseTask task) {
		try {
			if (task == null) {
				logger.error("task 不能为null!!");
				return;
			}
			// 登陆过滤
			if (filterLogin(task)) {
				return;
			}

			WorkerThread thread = getWorkerThread(task.getDispatchId());
			if (thread == null) {
				logger.error("获取不到工作线程！！！" + task.getDispatchId());
				return;
			}
			
			thread.addTask(task);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private boolean filterLogin(BaseTask task) {
		if (task instanceof MessageTask) {
			if (((MessageTask) task).isLogin()) {
				LOGIN_EXECUTOR.submit(task);
				return true;
			}
		}
		return false;
	}

	/** 获取工作线程 */
	private WorkerThread getWorkerThread(int dispatchId) {
		try {
			if (dispatchId == 0) {
				long count = noLoginCount.getAndIncrement();
				count &= Integer.MAX_VALUE;
				return noLoginWorker[(int) (count % NO_LOGON_CORE_SIZE)];
			}
			int index = Math.abs(dispatchId % CORE_SIZE);
			return loginWorker[index];
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
