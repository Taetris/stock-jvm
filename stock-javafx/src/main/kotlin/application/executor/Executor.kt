package application.executor

interface Executor {

    fun submit(runnable: Runnable)
}