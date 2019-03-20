package com.game.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.game.socket.thread.NameThreadFactory;

/**
 *  消息处理上下文
 * @author caiweikai
 * @date 2019年3月19日
 */
public class HandleContext {

	/** 处理器数量 */
	private final int CORE = Runtime.getRuntime().availableProcessors();
	/** 工作线程数量 */
	private final int CORE_SIZE = CORE * 2;
	/** 未登录工作线程数量 */
	private final int NO_LOGON_CORE_SIZE =  2;
	/** 登陆线程 */
	private ExecutorService LOGIN_EXECUTOR = Executors.newFixedThreadPool(2, new NameThreadFactory("login"));

	
}
