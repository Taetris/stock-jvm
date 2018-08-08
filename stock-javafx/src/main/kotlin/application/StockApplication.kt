package application

import injection.DaggerStockComponent
import injection.StockComponent
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import repository.customer.CustomerRepository
import javax.inject.Inject

class StockApplication : Application() {

    lateinit var stockComponent: StockComponent

    @Inject
    lateinit var customerRepository: CustomerRepository

    override fun start(primaryStage: Stage?) {
        stockComponent = DaggerStockComponent.create()
        stockComponent.inject(this)

        val main = FXMLLoader.load<Parent>(StockApplication::class.java.getResource("../../resources/stock-main.fxml"))
        val scene = Scene(main, WINDOW_WIDTH, WINDOW_HEIGHT)
        primaryStage?.scene = scene
        primaryStage?.show()

        customerRepository.getAllCustomers()
    }

    companion object {

        private const val WINDOW_WIDTH = 640.0
        private const val WINDOW_HEIGHT = 480.0

        @JvmStatic
        fun main(args: Array<String>) {
            launch(StockApplication::class.java)
        }
    }
}