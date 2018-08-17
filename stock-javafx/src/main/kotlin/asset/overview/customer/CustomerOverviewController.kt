package asset.overview.customer

import asset.add.customer.AddCustomerController
import asset.overview.customer.interactor.GetAllCustomersInteractor
import asset.overview.customer.interactor.GetAllCustomersOutput
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import repository.customer.Customer

class CustomerOverviewController : GetAllCustomersOutput {

    private val logger = LoggerFactory.getLogger(CustomerOverviewController::class.java)

    @FXML
    private lateinit var customersTable: TableView<Customer>
    @FXML
    private lateinit var idColumn: TableColumn<Customer, Number>
    @FXML
    private lateinit var nameColumn: TableColumn<Customer, String>
    @FXML
    private lateinit var accountNumberColumn: TableColumn<Customer, String>
    @FXML
    private lateinit var addressColumn: TableColumn<Customer, String>

    @FXML
    private lateinit var addCustomerButton: Button

    private val interactor = GetAllCustomersInteractor(this)

    @FXML
    fun initialize() {
        logger.info("Initialize")

        initializeListeners()
        initializeTable()

        interactor.getAllCustomers()
    }

    override fun onCustomersRetrieved(customers: List<Customer>) {
        customersTable.items.clear()
        customersTable.items = FXCollections.observableArrayList(customers)
    }

    override fun onRetrievalFailed(error: String) {
        customersTable.items.clear()
        customersTable.placeholder = Label(error)
    }

    private fun initializeListeners() {
        addCustomerButton.setOnAction {
            val stage = Stage()
            stage.scene = AddCustomerController.create()
            stage.showAndWait()
        }
    }

    private fun initializeTable() {
        idColumn.setCellValueFactory { param -> SimpleIntegerProperty(param.value.id) }
        nameColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.name) }
        accountNumberColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.accountNumber) }
        addressColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.address) }
    }
}