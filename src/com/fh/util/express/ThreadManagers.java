package com.fh.util.express;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**线程池管理的工具类   [废弃]
 * @ClassName:     ThreadManagers   
 * @Description:   TODO(线程池管理的工具类)   
 * @author:        Ajie
 * @date:          2019年9月24日 上午10:17:42     
 */
public class ThreadManagers {
	// 通过ThreadPoolExecutor的代理类来对线程池的管理
	private static ThreadPollProxy mThreadPollProxy; // 单列对象

	
	/**初始化线程池
	 * 默认5个核心线程数，最大10个线程，线程空闲时间为1秒
	 */
	public static ThreadPollProxy getThreadPollProxy() {
		synchronized (ThreadPollProxy.class) {
			if (mThreadPollProxy == null) {
				mThreadPollProxy = new ThreadPollProxy(5, 10, 1000);
			}
		}
		return mThreadPollProxy;
	}

	// 通过ThreadPoolExecutor的代理类来对线程池的管理
	public static class ThreadPollProxy {
		// 线程池执行者 ，java内部通过该api实现对线程池管理
		private ThreadPoolExecutor poolExecutor;
		// 核心线程的个数
		private int corePoolSize; 
		// 总的线程个数
		private int maximumPoolSize; 
		// 线程空闲的时间
		private long keepAliveTime; 

		public ThreadPollProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		} 
		
		/**对外提供一个执行任务的方法 
		 */
		public void execute(Runnable r) {
			if (poolExecutor == null || poolExecutor.isShutdown()) {
				poolExecutor = new ThreadPoolExecutor(
						// 核心线程数量
						corePoolSize,
						// 最大线程数量
						maximumPoolSize,
						// 当线程空闲时，保持活跃的时间
						keepAliveTime, // 时间单元 ，
						// 毫秒级
						TimeUnit.MILLISECONDS,
						// 线程任务队列
						new LinkedBlockingQueue<Runnable>(),
						// 创建线程的工厂
						Executors.defaultThreadFactory());
			}
			poolExecutor.execute(r);
		}
	}

	/**
	 * 功能描述：关闭线程池
	 * @author Ajie
	 * @date 2019/12/13 0013
	 */
	public static void shutDown() {
		getThreadPollProxy().poolExecutor.shutdownNow();
	}



}
