package asset.item.overview

import application.StockApplication
import application.executor.UI
import application.usecase.UseCaseException
import asset.item.subject.ItemObserver
import asset.item.subject.ItemSubject
import asset.item.usecase.GetAllItemsInteractor
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.item.Item
import javax.inject.Inject

class ItemOverviewController : ItemObserver {

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(ItemOverviewController::class.java)

    @FXML
    private lateinit var itemsTable: TableView<Item>
    @FXML
    private lateinit var idColumn: TableColumn<Item, Number>
    @FXML
    private lateinit var nameColumn: TableColumn<Item, String>
    @FXML
    private lateinit var dimensionColumn: TableColumn<Item, String>
    @FXML
    private lateinit var descriptionColumn: TableColumn<Item, String>
    @FXML
    private lateinit var amountColumn: TableColumn<Item, Number>
    @FXML
    private lateinit var unitColumn: TableColumn<Item, String>
    @FXML
    private lateinit var quantityColumn: TableColumn<Item, Number>
    @FXML
    private lateinit var pricePerUnitColumn: TableColumn<Item, Number>
    @FXML
    private lateinit var totalPriceWithoutTaxColumn: TableColumn<Item, Number>
    @FXML
    private lateinit var addItemButton: Button
    @FXML
    private lateinit var removeItemButton: Button

    @Inject
    internal lateinit var getAllItemsInteractor: GetAllItemsInteractor
    @Inject
    internal lateinit var subject: ItemSubject

    @FXML
    fun initialize() {
        logger.info("Initialize")

        initializeListeners()
        initializeTable()

        fetchItems()
    }

    override fun onItemsChanged() {
        fetchItems()
    }

    private fun initializeListeners() {
        subject.register(this)
    }

    private fun initializeTable() {
        idColumn.setCellValueFactory { param -> SimpleIntegerProperty(param.value.id) }
        nameColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.name) }
        dimensionColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.dimension.toString()) }
        descriptionColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.description) }
        amountColumn.setCellValueFactory { param -> SimpleIntegerProperty(param.value.amount) }
        unitColumn.setCellValueFactory { param -> SimpleStringProperty(param.value.dimension.unit.value) }
        quantityColumn.setCellValueFactory { param -> SimpleDoubleProperty(param.value.calculateQuantity()) }
        pricePerUnitColumn.setCellValueFactory { param -> SimpleDoubleProperty(param.value.pricePerUnit) }
        totalPriceWithoutTaxColumn.setCellValueFactory { param -> SimpleDoubleProperty(param.value.calculateTotalPriceWithoutTax()) }
    }

    private fun fetchItems() {
        itemsTable.items.clear()

        launch(UI) {
            try {
                val items = getAllItemsInteractor.getAllItems()
                itemsTable.items = FXCollections.observableArrayList(items)
            } catch (e: UseCaseException) {
                itemsTable.placeholder = Label("Failed to retrieve data. Error: ${e.message}.")
            }
        }
    }
}