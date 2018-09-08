package invoice.items.overview

import application.ResourceLoader
import application.StockApplication
import application.executor.UI
import application.usecase.ErrorCodeMapper
import application.usecase.UseCaseException
import asset.item.error.ItemErrorCodeMapper
import asset.item.usecase.GetItemUseCase
import invoice.items.select.SelectItemController
import invoice.model.Invoice
import invoice.usecase.GenerateInvoiceUseCase
import invoice.usecase.UpdateItemsUseCase
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextInputDialog
import javafx.scene.layout.Pane
import javafx.stage.FileChooser
import javafx.stage.Stage
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.item.Item
import view.dialog.DialogUtil
import view.formatter.TextToIntFormatter
import java.io.File
import javax.inject.Inject


class SelectedItemsOverviewController {

    companion object {

        fun create(invoice: Invoice): Scene {
            val loader = ResourceLoader.loader(SelectedItemsOverviewController::class.java, "invoice/stock-invoice-item-overview.fxml")
            val view = loader.load<Pane>()
            val controller = loader.getController<SelectedItemsOverviewController>()
            controller.initialize(invoice)
            return Scene(view)
        }
    }

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(SelectedItemsOverviewController::class.java)

    @Inject
    internal lateinit var getItemUseCase: GetItemUseCase
    @Inject
    internal lateinit var generateInvoiceUseCase: GenerateInvoiceUseCase
    @Inject
    internal lateinit var updateItemsUseCase: UpdateItemsUseCase

    @FXML
    private lateinit var selectedItemsTable: TableView<Item>
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
    private lateinit var removeItemButton: Button
    @FXML
    private lateinit var selectItemButton: Button
    @FXML
    private lateinit var finishButton: Button

    private lateinit var invoice: Invoice

    private fun initialize(invoice: Invoice) {
        logger.info("Initialize with invoice '$invoice'")
        this.invoice = invoice

        initializeControls()
        initializeTable()
    }

    private fun initializeControls() {
        finishButton.setOnAction {
            val file = chooseFileToSave()
            if (file != null) {
                generateInvoice(file)
            }
        }
        removeItemButton.setOnAction {
            val selectedItem = selectedItemsTable.selectionModel.selectedItem
            removeSelectedItem(selectedItem)
        }
        selectItemButton.setOnAction {
            val itemId = inputItemId()
            fetchItemAndNavigateToSelect(itemId)
        }

        removeItemButton.isVisible = false
        removeItemButton.visibleProperty().bind(selectedItemsTable.selectionModel.selectedItemProperty().isNotNull)
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

        selectedItemsTable.items = invoice.selectedItems
    }

    private fun chooseFileToSave(): File? {
        val fileChooser = FileChooser()
        val extension = FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")
        fileChooser.extensionFilters.addAll(extension)

        return fileChooser.showSaveDialog(finishButton.scene.window as Stage)
    }

    private fun generateInvoice(file: File) {
        launch(UI) {
            try {
                generateInvoiceUseCase.generateInvoice(invoice, file)
                updateItemsUseCase.updateItems(invoice.selectedItems.toList())
                close()
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(ErrorCodeMapper().mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun removeSelectedItem(selectedItem: Item) = invoice.selectedItems.remove(selectedItem)

    private fun inputItemId(): Int {
        val dialog = TextInputDialog()
        dialog.headerText = ResourceLoader.bundle.getString("selectItemDialogHeader")
        dialog.contentText = ResourceLoader.bundle.getString("selectItemDialogContent")
        dialog.editor.textFormatter = TextToIntFormatter()

        val result = dialog.showAndWait()
        return result.get().toInt()
    }

    private fun fetchItemAndNavigateToSelect(id: Int) {
        launch(UI) {
            try {
                val item = getItemUseCase.getItem(id)
                navigateToSelect(item)
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(ItemErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun navigateToSelect(item: Item) {
        if (item.amount <= 0) {
            DialogUtil.showErrorDialog("Roba sa ID-om '${item.id}' trenutno nije na stanju")
            return
        }

        val scene = SelectItemController.create(invoice, item)
        val stage = Stage()
        stage.scene = scene
        stage.show()
    }

    private fun close() = (finishButton.scene.window as Stage).close()
}