package application.executor

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

class WorkerExecutor : Executor {

    private val threadPoolExecutor: ExceptionHandlingThreadPoolExecutor

    init {
        val numberOfCores = Runtime.getRuntime().availableProcessors()
        val blockingDeque = LinkedBlockingDeque<Runnable>()
        threadPoolExecutor = ExceptionHandlingThreadPoolExecutor(numberOfCores, numberOfCores, 1, TimeUnit.SECONDS, blockingDeque)
    }

    override fun submit(runnable: Runnable) {
        threadPoolExecutor.submit(runnable)
    }
}