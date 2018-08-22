package application.executor

import javafx.application.Platform
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Runnable
import kotlin.coroutines.experimental.CoroutineContext

val UI = application.executor.UIDispatcher()

class UIDispatcher : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        Platform.runLater(block)
    }
}