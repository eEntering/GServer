package com.game.socket.dispatch;

/**
 *  线路分发器，用来执行业务时选取线程的参数
 * @author caiweikai
 * @date 2019年3月20日
 */
public interface IGameDispatch {

	/** 线路 */
	int dispatchLine();
	
	/** 地图 */
	int dispatchMap();
	
	/** 匿名分发器，作为没登陆的玩家使用 */
	public class AnonymousDispatch implements IGameDispatch{

		@Override
		public int dispatchLine() {
			return 0;
		}

		@Override
		public int dispatchMap() {
			return 0;
		}
		
	}
}
