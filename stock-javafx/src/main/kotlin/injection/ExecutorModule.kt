package injection

import application.executor.Executor
import application.executor.MainExecutor
import application.executor.WorkerExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ExecutorModule {

    @Singleton
    @Provides
    @Named("Main")
    fun provideMainExecutor(): Executor {
        return MainExecutor()
    }

    @Singleton
    @Provides
    @Named("Worker")
    fun provideWorkerExecutor(): Executor {
        return WorkerExecutor()
    }
}