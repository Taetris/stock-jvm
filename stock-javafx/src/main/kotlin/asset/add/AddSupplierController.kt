package asset.add

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.slf4j.LoggerFactory

class AddSupplierController {

    private val logger = LoggerFactory.getLogger(AddSupplierController::class.java)

    @FXML
    lateinit var cancelButton: Button
    @FXML
    lateinit var addButton: Button

    @FXML
    fun initialize() {
        logger.info("Initialize")

        cancelButton.setOnAction { (cancelButton.scene.window as Stage).close() }
    }

    companion object {

        fun createView(): Pane {
            return FXMLLoader.load<Pane>(AddSupplierController::class.java.getResource("../../../resources/asset/stock-add-supplier.fxml"))
        }
    }
}