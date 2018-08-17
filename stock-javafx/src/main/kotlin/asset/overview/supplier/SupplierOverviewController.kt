package asset.overview.supplier

import asset.add.supplier.AddSupplierController
import asset.overview.supplier.interactor.GetAllSuppliersInteractor
import asset.overview.supplier.interactor.GetAllSuppliersOutput
import asset.subject.supplier.SupplierObserver
import asset.subject.supplier.SupplierSubject
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
import repository.supplier.Supplier

class SupplierOverviewController : GetAllSuppliersOutput, SupplierObserver {

    private val logger = LoggerFactory.getLogger(SupplierOverviewController::class.java)

    @FXML
    private lateinit var suppliersTable: TableView<Supplier>
    @FXML
    private lateinit var idColumn: TableColumn<Supplier, Number>
    @FXML
    private lateinit var nameColumn: TableColumn<Supplier, String>
    @FXML
    private lateinit var accountNumberColumn: TableColumn<Supplier, String>
    @FXML
    private lateinit var addressColumn: TableColumn<Supplier, String>

    @FXML
    private lateinit var addSupplierButton: Button

    private val interactor = GetAllSuppliersInteractor(this)
    private val subject = SupplierSubject()

    @FXML
    fun initialize() {
        logger.info("Initialize")

        initializeListeners()
        initializeTable()

        fetchSuppliers()
    }

    override fun onSuppliersRetrieved(suppliers: List<Supplier>) {
        suppliersTable.items.clear()
        suppliersTable.items = FXCollections.observableArrayList(suppliers)
    }

    override fun onRetrievalFailed(error: String) {
        suppliersTable.items.clear()
        suppliersTable.placeholder = Label(error)
    }

    override fun onSuppliersChanged() {
        fetchSuppliers()
    }

    private fun fetchSuppliers() {
        suppliersTable.items.clear()
        interactor.getAllSuppliers()
    }

    private fun initializeListeners() {
        addSupplierButton.setOnAction {
            val stage = Stage()
            stage.scene = AddSupplierController.create()
            stage.showAndWait()
        }

        subject.register(this)
    }

    private fun initializeTable() {
        idColumn.setCellValueFactory { param -> SimpleIntegerProperty(param.value.id) }
        nameColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.name) }
        accountNumberColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.accountNumber) }
        addressColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.address) }
    }
}