package application.executor;

import java.util.concurrent.*;

/**
 * Exception safe implementation of {@link ThreadPoolExecutor}.
 */
class ExceptionHandlingThreadPoolExecutor extends ThreadPoolExecutor {

    ExceptionHandlingThreadPoolExecutor(int corePoolSize,
                                        int maximumPoolSize,
                                        long keepAliveTime,
                                        TimeUnit unit,
                                        BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void afterExecute(Runnable runnable, Throwable throwable) {
        super.afterExecute(runnable, throwable);

        if (throwable == null && runnable instanceof Future<?>) {
            try {
                ((Future<?>) runnable).get();
            } catch (InterruptedException e) {
                throwable = e.getCause();
            } catch (ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (throwable != null) {
            throw new RuntimeException(throwable.getCause());
        }
    }
}
