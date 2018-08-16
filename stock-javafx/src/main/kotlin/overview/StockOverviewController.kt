package overview

import javafx.fxml.FXML
import javafx.scene.control.TabPane
import org.slf4j.LoggerFactory
import overview.view.customer.CustomerTabTemplate
import overview.view.supplier.SupplierTabTemplate
class StockOverviewController {

    private val logger = LoggerFactory.getLogger(StockOverviewController::class.java)

    @FXML
    private lateinit var customTabPane: TabPane

    private val customerTab = CustomerTabTemplate()
    private val supplierTab = SupplierTabTemplate()

    @FXML
    fun initialize() {
        logger.info("Initializing")

        customTabPane.tabs.add(customerTab.generateView())
        customTabPane.tabs.add(supplierTab.generateView())
    }
}