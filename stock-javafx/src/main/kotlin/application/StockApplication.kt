package application

import injection.DaggerStockComponent
import injection.StockComponent
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import java.util.*

class StockApplication : Application() {

    override fun start(primaryStage: Stage?) {
        stockComponent = DaggerStockComponent.create()

        val main = ResourceLoader.load(javaClass, "main/stock-main.fxml")
        val scene = Scene(main, WINDOW_WIDTH, WINDOW_HEIGHT)
        primaryStage?.scene = scene
        primaryStage?.setOnCloseRequest {
            Platform.exit()
            System.exit(0)
        }
        primaryStage?.show()
    }

    companion object {

        lateinit var stockComponent: StockComponent

        private const val WINDOW_WIDTH = 640.0
        private const val WINDOW_HEIGHT = 480.0

        @JvmStatic
        fun main(args: Array<String>) {
            Locale.setDefault(Locale("bs"))
            launch(StockApplication::class.java)
        }
    }
}