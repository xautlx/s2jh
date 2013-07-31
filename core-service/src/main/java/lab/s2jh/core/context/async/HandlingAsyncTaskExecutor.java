package lab.s2jh.core.context.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *  用于增强Spring @Async 注解在异步模式能捕捉到异常日志
 *  <bean id="handlingAsyncTaskExecutor" class="lab.s2jh.core.context.async.HandlingAsyncTaskExecutor"/>
 *  <task:annotation-driven executor="handlingAsyncTaskExecutor" />
 */
public class HandlingAsyncTaskExecutor implements AsyncTaskExecutor {

    private final static Logger logger = LoggerFactory.getLogger(HandlingAsyncTaskExecutor.class);

    private ThreadPoolTaskExecutor executor;

    public HandlingAsyncTaskExecutor() {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(7);
        executor.setMaxPoolSize(42);
        executor.setQueueCapacity(11);
        executor.setThreadNamePrefix("HandlingAsyncTaskExecutor-");
        executor.initialize();
    }

    private void handle(Exception e) {
        logger.error("Async method exception", e);
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        executor.execute(createWrappedRunnable(task), startTimeout);

    }

    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(createWrappedRunnable(task));
    }

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        return executor.submit(createCallable(task));
    }

    private <T> Callable<T> createCallable(final Callable<T> task) {
        return new Callable<T>() {
            @Override
            public T call() throws Exception {
                try {
                    return task.call();
                } catch (Exception e) {
                    handle(e);
                    throw e;
                }
            }
        };
    }

    private Runnable createWrappedRunnable(final Runnable task) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    handle(e);
                }
            }
        };
    }
}