package overview

import javafx.fxml.FXML
import javafx.scene.control.TabPane
import org.slf4j.LoggerFactory
import overview.tab.TabView
import repository.customer.Customer

class StockOverviewController {

    private val logger = LoggerFactory.getLogger(StockOverviewController::class.java)

    @FXML
    private lateinit var customTabPane: TabPane

    @FXML
    fun initialize() {
        logger.info("Initializing")

        val sampleView = object : TabView<Customer>() {

            override fun getTabName(): String {
                return "Customers"
            }

            override fun getButtonName(): String {
                return "Add Customer"
            }

            override fun getSearchHint(): String {
                return "Search for Customers..."
            }
        }

        customTabPane.tabs.add(sampleView.generateView())
    }
}