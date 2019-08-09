package thread;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadPool {

	/**
	 * 核心线程池大小
	 */
	static int corePoolSize = 4;
	/**
	 * 最大线程池大小
	 */
	static int maximumPoolSize = 16;
	/**
	 * 线程最大空闲时间
	 */
	static long keepAliveTime = 60;
	/**
	 * 时间单位
	 */
	static TimeUnit unit = TimeUnit.SECONDS;
	/**
	 * 线程等待队列
	 */
	static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(256);
	/**
	 * 线程创建工厂
	 */
	static ThreadFactory threadFactory = new NameTreadFactory();
	/**
	 * 拒绝策略
	 */
	static RejectedExecutionHandler handler = new MyIgnorePolicy();

	public static ExecutorService newThreadPool() throws InterruptedException, IOException {
		/**
		 * 执行器
		 */
		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
				workQueue, threadFactory, handler);
		executor.prestartAllCoreThreads(); // 预启动所有核心线程
		return executor;
	}

	private static class NameTreadFactory implements ThreadFactory {

		private final AtomicInteger mThreadNum = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "thread-" + mThreadNum.getAndIncrement());
			return t;
		}
	}

	private static class MyIgnorePolicy implements RejectedExecutionHandler {

		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			doLog(r, e);
		}

		private void doLog(Runnable r, ThreadPoolExecutor e) {
			// 可做日志记录等
			System.err.println(r.toString() + " rejected");
			// System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
		}
	}

}