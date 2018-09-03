package asset.customer.overview

import application.ResourceLoader
import application.StockApplication
import application.executor.UI
import application.usecase.UseCaseException
import asset.customer.error.CustomerErrorCodeMapper
import asset.customer.manage.ManageCustomerController
import asset.customer.subject.CustomerObserver
import asset.customer.subject.CustomerSubject
import asset.customer.usecase.GetAllCustomersUseCase
import asset.customer.usecase.RemoveCustomerUseCase
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.Stage
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.customer.Customer
import view.dialog.DialogUtil
import view.formatter.TextToIntFormatter
import javax.inject.Inject

class CustomerOverviewController : CustomerObserver {

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(CustomerOverviewController::class.java)

    @Inject
    internal lateinit var getAllCustomersUseCase: GetAllCustomersUseCase
    @Inject
    internal lateinit var removeCustomerUseCase: RemoveCustomerUseCase
    @Inject
    internal lateinit var subject: CustomerSubject

    @FXML
    private lateinit var customersTable: TableView<Customer>
    @FXML
    private lateinit var idColumn: TableColumn<Customer, Number>
    @FXML
    private lateinit var nameColumn: TableColumn<Customer, String>
    @FXML
    private lateinit var addressColumn: TableColumn<Customer, String>
    @FXML
    private lateinit var idNumberColumn: TableColumn<Customer, String>
    @FXML
    private lateinit var pdvNumberColumn: TableColumn<Customer, String>
    @FXML
    private lateinit var manageCustomerButton: Button
    @FXML
    private lateinit var removeCustomerButton: Button

    @FXML
    fun initialize() {
        logger.info("Initialize")

        initializeControls()
        initializeTable()

        fetchCustomers()
    }

    override fun onCustomersChanged() {
        fetchCustomers()
    }

    private fun initializeControls() {
        removeCustomerButton.setOnAction {
            val selectedCustomer = customersTable.selectionModel.selectedItem
            removeCustomer(selectedCustomer)
        }
        manageCustomerButton.setOnAction {
            val customerId = inputCustomerId()
            navigateToCustomerManagement(customerId)
        }

        removeCustomerButton.isVisible = false
        removeCustomerButton.visibleProperty().bind(customersTable.selectionModel.selectedItemProperty().isNotNull)

        subject.register(this)
    }

    private fun initializeTable() {
        idColumn.setCellValueFactory { param -> SimpleIntegerProperty(param.value.id) }
        nameColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.name) }
        idNumberColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.idNumber) }
        pdvNumberColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.pdvNumber) }
        addressColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.address) }
    }

    private fun fetchCustomers() {
        customersTable.items.clear()

        launch(UI) {
            try {
                val customers = getAllCustomersUseCase.getAllCustomers()
                customersTable.items = FXCollections.observableArrayList(customers)
            } catch (e: UseCaseException) {
                customersTable.placeholder = Label(CustomerErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun removeCustomer(selectedCustomer: Customer) {
        launch(UI) {
            try {
                removeCustomerUseCase.removeCustomer(selectedCustomer)
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(CustomerErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun inputCustomerId(): Int? {
        val dialog = TextInputDialog()
        dialog.headerText = ResourceLoader.bundle.getString("manageCustomerDialogHeader")
        dialog.contentText = ResourceLoader.bundle.getString("manageCustomerDialogContent")
        dialog.editor.textFormatter = TextToIntFormatter()

        val result = dialog.showAndWait()
        return result.get().toInt()
    }

    private fun navigateToCustomerManagement(customerId: Int?) {
        val stage = Stage()
        val scene = if (customerId == null) ManageCustomerController.create() else ManageCustomerController.create(customerId)
        stage.scene = scene
        stage.show()
    }
}