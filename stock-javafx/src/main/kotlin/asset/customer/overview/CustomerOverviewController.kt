package asset.customer.overview

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
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.stage.Stage
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.customer.Customer
import view.dialog.DialogUtil
import javax.inject.Inject

class CustomerOverviewController : CustomerObserver {

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(CustomerOverviewController::class.java)

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
    private lateinit var addCustomerButton: Button
    @FXML
    private lateinit var removeCustomerButton: Button
    @FXML
    private lateinit var editCustomerButton: Button

    @Inject
    lateinit var getAllCustomersUseCase: GetAllCustomersUseCase
    @Inject
    lateinit var removeCustomerUseCase: RemoveCustomerUseCase
    @Inject
    lateinit var subject: CustomerSubject

    @FXML
    fun initialize() {
        logger.info("Initialize")

        initializeListeners()
        initializeTable()

        fetchCustomers()
    }

    override fun onCustomersChanged() {
        fetchCustomers()
    }

    private fun initializeListeners() {
        addCustomerButton.setOnAction { addCustomer() }
        removeCustomerButton.setOnAction { removeCustomer() }
        editCustomerButton.setOnAction { editCustomer() }

        setButtonVisibility()

        subject.register(this)
    }

    private fun addCustomer() {
        val stage = Stage()
        stage.scene = ManageCustomerController.createForAdd()
        stage.show()
    }

    private fun removeCustomer() {
        launch(UI) {
            val selectedCustomer = customersTable.selectionModel.selectedItem

            try {
                removeCustomerUseCase.removeCustomer(selectedCustomer)
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(CustomerErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun editCustomer() {
        val stage = Stage()
        stage.scene = ManageCustomerController.createForUpdate(customersTable.selectionModel.selectedItem.id)
        stage.show()
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

    private fun initializeTable() {
        idColumn.setCellValueFactory { param -> SimpleIntegerProperty(param.value.id) }
        nameColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.name) }
        idNumberColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.idNumber) }
        pdvNumberColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.pdvNumber) }
        addressColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.address) }
    }

    private fun setButtonVisibility() {
        removeCustomerButton.isVisible = false
        editCustomerButton.isVisible = false

        customersTable.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            logger.info("Changed selection to $newValue")
            newValue?.let {
                removeCustomerButton.isVisible = true
                editCustomerButton.isVisible = true
            } ?: run {
                removeCustomerButton.isVisible = false
                editCustomerButton.isVisible = false
            }
        }
    }
}