package asset.overview.item

import asset.add.item.AddItemController
import asset.overview.item.interactor.GetAllItemsInteractor
import asset.overview.item.interactor.GetAllItemsOutput
import asset.subject.item.ItemObserver
import asset.subject.item.ItemSubject
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
import repository.item.Item

class ItemOverviewController : GetAllItemsOutput, ItemObserver {

    private val logger = LoggerFactory.getLogger(ItemOverviewController::class.java)

    @FXML
    private lateinit var itemsTable: TableView<Item>
    @FXML
    private lateinit var idColumn: TableColumn<Item, Number>
    @FXML
    private lateinit var nameColumn: TableColumn<Item, String>
    @FXML
    private lateinit var accountNumberColumn: TableColumn<Item, String>
    @FXML
    private lateinit var addressColumn: TableColumn<Item, String>

    @FXML
    private lateinit var addItemButton: Button

    private val interactor = GetAllItemsInteractor(this)
    private val subject = ItemSubject()

    @FXML
    fun initialize() {
        logger.info("Initialize")

        initializeListeners()
        initializeTable()

        fetchItems()
    }

    override fun onItemsRetrieved(items: List<Item>) {
        itemsTable.items.clear()
        itemsTable.items = FXCollections.observableArrayList(items)
    }

    override fun onRetrievalFailed(error: String) {
        itemsTable.items.clear()
        itemsTable.placeholder = Label(error)
    }

    override fun onItemsChanged() {
        fetchItems()
    }

    private fun fetchItems() {
        itemsTable.items.clear()
        interactor.getAllItems()
    }

    private fun initializeListeners() {
        addItemButton.setOnAction {
            val stage = Stage()
            stage.scene = AddItemController.create()
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