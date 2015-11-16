package com.renren.ugc.comment.cache;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import com.renren.ugc.comment.task.CommentTaskDispatcherClient;

/**
 * 标注 :{@link AsyncTaskDispatcher} 异步任务调度器
 * 
 * @author xinyan.yang@renren-inc.com
 * @Deprecated 异步任务使用 {@link CommentTaskDispatcherClient} 
 * @2013-1-30 10:57:31
 */
//TODO........异步缓存,待压测后看性能瓶颈
 
public class AsyncTaskDispatcher<T> {

	/**
	 * 默认的最大任务队列长度
	 */
	public static final int DEFAULT_MAX_TASK_QUEUE_SIZE = 100000;	//10W
	
	/**
	 * 当任务队列满的时候是否丢弃后来的任务，默认true
	 */
	public static final boolean DEFAULT_DROP_TASK_ON_QUEUE_FULL = true;
	
	/**
	 * 一个批处理任务的长度
	 */
	public static final int DEFAULT_BATCH_SIZE = 1000;
	
	/**
	 * 默认的工作线程池的大小
	 */
	public static final int DEFAULT_THREAD_POOL_SIZE = 2;
	
	public static final long DEFAULT_TAKE_TIMEOUT = 2000;	//单位ms
	
	/**
	 * 干活的人
	 */
	private ITaskHandler<T> taskHandler;
	
	/**
	 * 任务队列满了之后是否丢弃任务，如果不丢弃就会阻塞
	 */
	//private boolean dropTaskOnQueueFull;
	
	/**
	 * 一次批处理所处理的任务的个数
	 */
	private int batchSize;
	
	private long takeTimeout = DEFAULT_TAKE_TIMEOUT;
	
	
	/**
	 * 阻塞的任务队列
	 */
	private BlockingQueue<T> taskQueue;
	
	/**
	 * 工作线程池
	 */
	private ExecutorService threadPool;
	
	/**
	 * 任务调度线程
	 */
	/*private Thread dispatcher = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) { 
				try {
					List<T> batch = new ArrayList<T>(batchSize);
					int count = 0;
					while (count++ < batchSize) {
						T task = taskQueue.poll(takeTimeout, TimeUnit.MILLISECONDS);
						if (task != null) {
							batch.add(task);
						} else {	//poll超时的时候会返回null
							//System.out.println("...");
							break;
						}
					}
					doBatchWork(batch);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}			
		}
	});*/
	
	/*public AsyncTaskDispatcher(ITaskHandler<T> worker) {
		this(worker,DEFAULT_MAX_TASK_QUEUE_SIZE, DEFAULT_DROP_TASK_ON_QUEUE_FULL, DEFAULT_BATCH_SIZE, DEFAULT_THREAD_POOL_SIZE);
	}
	
	public AsyncTaskDispatcher(ITaskHandler<T> worker, int maxTaskQueueSize,
			boolean dropTaskOnQueueFull, int batchSize, int threadPoolSize) {
		this(worker, maxTaskQueueSize, dropTaskOnQueueFull, batchSize, threadPoolSize, DEFAULT_TAKE_TIMEOUT);
	}*/
	
	/**
	 * @param worker 干活的人
	 * @param taskQueueSize 任务队列的长度
	 * @param dropTaskOnQueueFull 任务队列满了之后是否丢弃任务，如果不丢弃就会阻塞
	 * @param batchSize 一次批处理所处理的任务的个数
	 * @param takeTimeout 收集batch时的timeout时间
	 */
	/*public AsyncTaskDispatcher(ITaskHandler<T> worker, int maxTaskQueueSize,
			boolean dropTaskOnQueueFull, int batchSize, int threadPoolSize,
			long takeTimeout) {
		this.taskHandler = worker;
		this.dropTaskOnQueueFull = dropTaskOnQueueFull;
		this.batchSize = batchSize;
		this.takeTimeout = takeTimeout;
		//this.maxTaskQueueSize = maxTaskQueueSize;
		taskQueue = new LinkedBlockingQueue<T>(maxTaskQueueSize);
		threadPool = Executors.newFixedThreadPool(threadPoolSize);
		startTaskDispatcher();
	}*/
	
	/**
	 * 启动任务调度线程
	 */
	/*private void startTaskDispatcher() {
		dispatcher.setDaemon(true);
		dispatcher.start();
	}*/
	
	/**
	 * 处理批量任务
	 * @param batch
	 */
	/*private void doBatchWork(final List<T> batch) {
		if (batch == null || batch.size() == 0) {
			return;
		}
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					taskHandler.handle(batch);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}*/
	
	/**
	 * 提交一个任务
	 * @param task
	 */
	/*public void submit(T task) {
		if (dropTaskOnQueueFull) {
			boolean flag = taskQueue.offer(task);
			if (!flag) {
				System.err.println("AsyncTaskDispatcher queue full, drop task:" + task);
			}
		} else {
			try {
				taskQueue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}*/
	
}
