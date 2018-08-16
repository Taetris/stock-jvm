package application

import injection.DaggerStockComponent
import injection.StockComponent
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class StockApplication : Application() {

    override fun start(primaryStage: Stage?) {
        stockComponent = DaggerStockComponent.create()
        val main = FXMLLoader.load<Parent>(StockApplication::class.java.getResource("../../resources/stock-main.fxml"))
        val scene = Scene(main, WINDOW_WIDTH, WINDOW_HEIGHT)
        primaryStage?.scene = scene
        primaryStage?.show()
    }

    companion object {

        lateinit var stockComponent: StockComponent

        private const val WINDOW_WIDTH = 640.0
        private const val WINDOW_HEIGHT = 480.0

        @JvmStatic
        fun main(args: Array<String>) {
            launch(StockApplication::class.java)
        }
    }
}