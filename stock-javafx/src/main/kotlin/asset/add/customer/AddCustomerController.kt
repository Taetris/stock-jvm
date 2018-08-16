package asset.add.customer

import asset.add.customer.interactor.AddCustomerInteractor
import asset.add.customer.interactor.AddCustomerOutput
import asset.add.supplier.AddSupplierController
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import util.Dialog
import util.NumberTextFormatter

class AddCustomerController : AddCustomerOutput {

    companion object {

        fun createView(): Pane {
            return FXMLLoader.load<Pane>(AddCustomerController::class.java.getResource("../../../../resources/asset/stock-add-customer.fxml"))
        }
    }

    private val logger = LoggerFactory.getLogger(AddSupplierController::class.java)

    @FXML
    private lateinit var cancelButton: Button
    @FXML
    private lateinit var addButton: Button
    @FXML
    private lateinit var idTextField: TextField
    @FXML
    private lateinit var nameTextField: TextField
    @FXML
    private lateinit var accountNumberTextField: TextField
    @FXML
    private lateinit var addressTextField: TextField

    private val interactor = AddCustomerInteractor(this)

    @FXML
    fun initialize() {
        logger.info("Initialize")

        setListeners()
    }

    override fun onInsertionSuccessful() {
        (cancelButton.scene.window as Stage).close()
    }

    override fun onInsertionFailed(error: String) {
        Dialog.showErrorDialog(header = "Failed to insert supplier", content = error)
    }

    private fun setListeners() {
        idTextField.textFormatter = NumberTextFormatter()

        cancelButton.setOnAction { (cancelButton.scene.window as Stage).close() }
        addButton.setOnAction {
            interactor.addNewCustomer(
                    idTextField.text.toInt(),
                    nameTextField.text,
                    accountNumberTextField.text,
                    addressTextField.text)
        }
    }
}