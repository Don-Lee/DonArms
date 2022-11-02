package cn.rjgc.commonlib.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局线程管理者
 * 参考https://juejin.cn/post/6948034657321484318
 *
 * @author donle
 */
public class AppThreadExecutors {

    /**
     * CPU数量（核心线程数）获取逻辑核心数，如6核心12线程，那么返回的是12
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2;

    /**
     * 单线程线程池(适用于串行执行任务的场景，一个任务一个任务地执行。)
     */
    private final Executor singleThread;
    /**
     * 固定线程数量线程池
     */
    private final Executor fixedThread;
    /**
     * 可缓存线程的线程池（适用于大量的需要立即处理的并且耗时较短的任务）
     * 没有核心线程，非核心线程数为Integer.MAX_VALUE。使用同步队列(SynchronousQueue)，不缓存任务，添加一个任务就会创建一个线程，极端情况下会创建过多的线程，耗尽 CPU 和内存资源
     */
    private final Executor cachedThread;
    /**
     * 主线程线程池
     */
    private final Executor mainThread;

    private AppThreadExecutors() {
        this(new SingleThreadExecutor(), new FixThreadExecutor(), new CachedThreadExecutor(), new MainThreadExecutor());
    }

    public AppThreadExecutors(Executor singleThread, Executor fixedThread, Executor cachedThread, Executor mainThread) {
        this.singleThread = singleThread;
        this.fixedThread = fixedThread;
        this.cachedThread = cachedThread;
        this.mainThread = mainThread;
    }

    public static AppThreadExecutors getInstance() {
        return AppThreadExecutorsHolder.INSTANCE;
    }

    public Executor getSingleThread() {
        return singleThread;
    }

    public Executor getFixedThread() {
        return fixedThread;
    }

    public Executor getCachedThread() {
        return cachedThread;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }

    private static class SingleThreadExecutor implements Executor {
        private final Executor mSingleThread;

        /**
         * 单线程 线程池(SingleThreadPoolExecutor)
         * LinkedBlockingQueue 无界的阻塞队列，理论上可以无限添加任务，有oom的风险
         */
        public SingleThreadExecutor() {
            mSingleThread = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    // 创建线程
                    Thread thread = new Thread(runnable);
                    // 设置线程名称
                    thread.setName("singleThread-Don");
                    return thread;
                }
            });
        }

        @Override
        public void execute(Runnable runnable) {
            mSingleThread.execute(runnable);
        }
    }

    private static class FixThreadExecutor implements Executor {
        private final Executor mFixThread;

        /**
         * 创建固定线程数量的线程池FixedThreadPool
         * LinkedBlockingQueue 无界的阻塞队列，理论上可以无限添加任务，有oom的风险
         */
        public FixThreadExecutor() {
            mFixThread = new ThreadPoolExecutor(CPU_COUNT, CPU_COUNT, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(), Executors.defaultThreadFactory());
        }

        @Override
        public void execute(Runnable runnable) {
            mFixThread.execute(runnable);
        }
    }

    private static class CachedThreadExecutor implements Executor {
        private final Executor mCachedThread;

        /**
         * 可缓存线程的线程池,可使用Executors.newCachedThreadPool()
         */
        public CachedThreadExecutor() {
            mCachedThread = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS,
                    new SynchronousQueue<>(), Executors.defaultThreadFactory());
        }

        @Override
        public void execute(Runnable runnable) {
            mCachedThread.execute(runnable);
        }
    }

    private static class AppThreadExecutorsHolder {
        private static final AppThreadExecutors INSTANCE = new AppThreadExecutors();
    }
}
