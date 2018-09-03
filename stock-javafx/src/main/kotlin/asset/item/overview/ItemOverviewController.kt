package asset.item.overview

import application.ResourceLoader
import application.StockApplication
import application.executor.UI
import application.usecase.UseCaseException
import asset.item.error.ItemErrorCodeMapper
import asset.item.manage.ManageItemController
import asset.item.subject.ItemObserver
import asset.item.subject.ItemSubject
import asset.item.usecase.GetAllItemsUseCase
import asset.item.usecase.RemoveItemUseCase
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.Stage
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.item.Item
import view.dialog.DialogUtil
import view.formatter.TextToIntFormatter
import javax.inject.Inject

class ItemOverviewController : ItemObserver {

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(ItemOverviewController::class.java)

    @Inject
    internal lateinit var getAllItemsUseCase: GetAllItemsUseCase
    @Inject
    internal lateinit var removeItemUseCase: RemoveItemUseCase
    @Inject
    internal lateinit var subject: ItemSubject

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
    private lateinit var manageItemButton: Button
    @FXML
    private lateinit var removeItemButton: Button

    @FXML
    fun initialize() {
        logger.info("Initialize")

        initializeControls()
        initializeTable()

        fetchItems()
    }

    override fun onItemsChanged() {
        fetchItems()
    }

    private fun initializeControls() {
        removeItemButton.setOnAction {
            val selectedItem = itemsTable.selectionModel.selectedItem
            removeItem(selectedItem)
        }
        manageItemButton.setOnAction {
            val itemId = inputItemId()
            navigateToItemManagement(itemId)
        }

        removeItemButton.isVisible = false
        removeItemButton.visibleProperty().bind(itemsTable.selectionModel.selectedItemProperty().isNotNull)

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
                val items = getAllItemsUseCase.getAllItems()
                itemsTable.items = FXCollections.observableArrayList(items)
            } catch (e: UseCaseException) {
                itemsTable.placeholder = Label(ItemErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun removeItem(item: Item) {
        launch(UI) {
            try {
                removeItemUseCase.removeItem(item)
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(ItemErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun inputItemId(): Int? {
        val dialog = TextInputDialog()
        dialog.headerText = ResourceLoader.bundle.getString("manageItemDialogHeader")
        dialog.contentText = ResourceLoader.bundle.getString("manageItemDialogContent")
        dialog.editor.textFormatter = TextToIntFormatter()

        val result = dialog.showAndWait()
        return result.get().toInt()
    }

    private fun navigateToItemManagement(itemId: Int?) {
        val stage = Stage()
        val scene = if (itemId == null) ManageItemController.create() else ManageItemController.create(itemId)
        stage.scene = scene
        stage.show()
    }
}