package asset.overview.customer

import asset.add.customer.AddCustomerController
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.stage.Stage
import org.slf4j.LoggerFactory

class CustomerOverviewController {

    private val logger = LoggerFactory.getLogger(CustomerOverviewController::class.java)

    @FXML
    private lateinit var addCustomerButton: Button

    @FXML
    fun initialize() {
        logger.info("Initialize")

        addCustomerButton.setOnAction {
            val stage = Stage()
            stage.scene = AddCustomerController.create()
            stage.showAndWait()
        }
    }
}