package injection

import application.executor.Executor
import application.executor.MainExecutor
import application.executor.WorkerExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class ExecutorModule {

    @Singleton
    @Provides
    @Named("main")
    fun provideMainExecutor(): Executor {
        return MainExecutor()
    }

    @Singleton
    @Provides
    @Named("worker")
    fun provideWorkerExecutor(): Executor {
        return WorkerExecutor()
    }
}