package invoice.metadata

import application.ResourceLoader
import javafx.fxml.FXML
import javafx.scene.Scene
import org.slf4j.LoggerFactory

class InvoiceMetadataController {

    companion object {

        fun create(): Scene {
            val view = ResourceLoader.load(InvoiceMetadataController::class.java, "invoice/stock-invoice-metadata.fxml")
            return Scene(view)
        }
    }

    private val logger = LoggerFactory.getLogger(InvoiceMetadataController::class.java)

    @FXML
    fun initialize() {
        logger.info("Initialize")
    }
}