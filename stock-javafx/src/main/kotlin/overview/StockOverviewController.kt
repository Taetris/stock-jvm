package overview

import javafx.fxml.FXML
import javafx.scene.control.Tab
import org.slf4j.LoggerFactory

class StockOverviewController {

    private val logger = LoggerFactory.getLogger(StockOverviewController::class.java)

    @FXML
    private lateinit var customersTab: Tab
    @FXML
    private lateinit var suppliersTab: Tab

    @FXML
    fun initialize() {
        logger.info("Initializing")

    }
}