package overview

import javafx.fxml.FXML
import javafx.scene.control.TabPane
import org.slf4j.LoggerFactory
import overview.tab.TabView
import overview.tab.customer.CustomerTab
import overview.tab.supplier.SupplierTab
import repository.customer.Customer

class StockOverviewController {

    private val logger = LoggerFactory.getLogger(StockOverviewController::class.java)

    @FXML
    private lateinit var customTabPane: TabPane

    private val customerTab = CustomerTab()
    private val supplierTab = SupplierTab()

    @FXML
    fun initialize() {
        logger.info("Initializing")

        customTabPane.tabs.add(customerTab.generateView())
        customTabPane.tabs.add(supplierTab.generateView())
    }
}