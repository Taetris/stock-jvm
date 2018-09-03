package asset.customer.manage

import application.ResourceLoader
import application.StockApplication
import application.executor.UI
import application.usecase.UseCaseException
import asset.customer.error.CustomerErrorCodeMapper
import asset.customer.usecase.AddCustomerUseCase
import asset.customer.usecase.GetCustomerUseCase
import asset.customer.usecase.UpdateCustomerUseCase
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.stage.Stage
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.customer.Customer
import view.dialog.DialogUtil
import view.formatter.NumberOnlyTextFormatter
import view.formatter.TextToIntFormatter
import javax.inject.Inject

class ManageCustomerController {

    companion object {

        private const val NO_CUSTOMER_ID = -1

        fun create(customerId: Int = NO_CUSTOMER_ID): Scene {
            val loader = ResourceLoader.loader(ManageCustomerController::class.java, "asset/customer/stock-manage-customer.fxml")
            val view = loader.load<Pane>()
            val controller = loader.getController<ManageCustomerController>()
            controller.initialize(customerId)
            return Scene(view)
        }
    }

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(ManageCustomerController::class.java)

    @Inject
    internal lateinit var addCustomerUseCase: AddCustomerUseCase
    @Inject
    internal lateinit var updateCustomerUseCase: UpdateCustomerUseCase
    @Inject
    internal lateinit var getCustomerUseCase: GetCustomerUseCase

    @FXML
    private lateinit var idTextField: TextField
    @FXML
    private lateinit var nameTextField: TextField
    @FXML
    private lateinit var addressTextField: TextField
    @FXML
    private lateinit var idNumberTextField: TextField
    @FXML
    private lateinit var pdvNumberTextField: TextField
    @FXML
    private lateinit var cancelButton: Button
    @FXML
    private lateinit var saveButton: Button

    private fun initialize(customerId: Int) {
        logger.info("Initializing view for customer id '$customerId'")

        initializeControls(customerId)
        initializeController(customerId)
    }

    private fun initializeControls(customerId: Int) {
        idTextField.textFormatter = TextToIntFormatter()
        idNumberTextField.textFormatter = NumberOnlyTextFormatter()
        pdvNumberTextField.textFormatter = NumberOnlyTextFormatter()
        cancelButton.setOnAction { close() }

        idTextField.text = customerId.toString()
    }

    private fun initializeController(id: Int) {
        launch(UI) {
            try {
                val customer = getCustomerUseCase.getCustomer(id)
                initializeForUpdate(customer)
            } catch (e: UseCaseException) {
                initializeForAdd()
            }
        }
    }

    private fun initializeForUpdate(customer: Customer) {
        idTextField.isDisable = true

        fillInMissingFields(customer)
        saveButton.setOnAction {
            updateCustomer()
            close()
        }
    }

    private fun initializeForAdd() {
        saveButton.setOnAction {
            addCustomer()
            close()
        }
    }

    private fun addCustomer() {
        launch(UI) {
            try {
                addCustomerUseCase.addNewCustomer(
                        id = idTextField.text.toInt(),
                        name = nameTextField.text,
                        idNumber = idNumberTextField.text,
                        pdvNumber = pdvNumberTextField.text,
                        address = addressTextField.text)
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(CustomerErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun updateCustomer() {
        launch(UI) {
            try {
                updateCustomerUseCase.updateCustomer(
                        id = idTextField.text.toInt(),
                        name = nameTextField.text,
                        idNumber = idNumberTextField.text,
                        pdvNumber = pdvNumberTextField.text,
                        address = addressTextField.text)
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(CustomerErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun fillInMissingFields(customer: Customer) {
        nameTextField.text = customer.name
        idNumberTextField.text = customer.idNumber
        pdvNumberTextField.text = customer.pdvNumber
        addressTextField.text = customer.address
    }

    private fun close() = (cancelButton.scene.window as Stage).close()
}