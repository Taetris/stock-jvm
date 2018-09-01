package menu

import invoice.metadata.InvoiceMetadataController
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import javafx.stage.Stage
import org.slf4j.LoggerFactory

class MenuController {

    private val logger = LoggerFactory.getLogger(MenuController::class.java)

    @FXML
    private lateinit var createInvoiceMenuItem: MenuItem
    @FXML
    private lateinit var exitMenuItem: MenuItem

    @FXML
    fun initialize() {
        logger.info("Initialize")

        createInvoiceMenuItem.setOnAction { createInvoice() }
        exitMenuItem.setOnAction { close() }
    }

    private fun createInvoice() {
        val stage = Stage()
        stage.scene = InvoiceMetadataController.create()
        stage.show()
    }

    private fun close() {
        Platform.exit()
        System.exit(0)
    }
}