package application.executor

import javafx.application.Platform

class MainExecutor : Executor {

    override fun submit(runnable: Runnable) {
        Platform.runLater(runnable)
    }
}