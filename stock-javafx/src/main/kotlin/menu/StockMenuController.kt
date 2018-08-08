package menu

import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import org.slf4j.LoggerFactory

class StockMenuController {

    private val logger = LoggerFactory.getLogger(StockMenuController::class.java)

    @FXML
    private lateinit var exportToPdfMenuItem: MenuItem
    @FXML
    private lateinit var saveAndCloseMenuItem: MenuItem
    @FXML
    private lateinit var calculateStockMenuItem: MenuItem
    @FXML
    private lateinit var aboutMenuItem: MenuItem

    @FXML
    fun initialize() {
        logger.info("Initializing")

        exportToPdfMenuItem.setOnAction { logger.info("exportToPdf") }
        saveAndCloseMenuItem.setOnAction { logger.info("saveAndClose") }
        calculateStockMenuItem.setOnAction { logger.info("calculateStock") }
        aboutMenuItem.setOnAction { logger.info("about") }
    }
}