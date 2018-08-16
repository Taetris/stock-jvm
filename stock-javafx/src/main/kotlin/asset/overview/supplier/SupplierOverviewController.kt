package asset.overview.supplier

import asset.add.supplier.AddSupplierController
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.stage.Stage
import org.slf4j.LoggerFactory

class SupplierOverviewController {

    private val logger = LoggerFactory.getLogger(SupplierOverviewController::class.java)

    @FXML
    private lateinit var addSupplierButton: Button

    @FXML
    fun initialize() {
        logger.info("Initialize")

        addSupplierButton.setOnAction {
            val stage = Stage()
            stage.scene = AddSupplierController.create()
            stage.showAndWait()
        }
    }
}