package asset.item.manage

import application.ResourceLoader
import application.StockApplication
import application.executor.UI
import application.usecase.UseCaseException
import asset.item.error.ItemErrorCodeMapper
import asset.item.usecase.AddItemUseCase
import asset.item.usecase.GetItemUseCase
import asset.item.usecase.UpdateItemUseCase
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.stage.Stage
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.item.Item
import view.dialog.DialogUtil
import view.formatter.DimensionTextFormatter
import view.formatter.TextToDoubleFormatter
import view.formatter.TextToIntFormatter
import view.formatter.UnitTextFormatter
import javax.inject.Inject

class ManageItemController {

    companion object {

        private const val NO_ITEM_ID = -1

        fun create(itemId: Int = NO_ITEM_ID): Scene {
            val loader = ResourceLoader.loader(ManageItemController::class.java, "asset/item/stock-manage-item.fxml")
            val view = loader.load<Pane>()
            val controller = loader.getController<ManageItemController>()
            controller.initialize(itemId)
            return Scene(view)
        }
    }

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(ManageItemController::class.java)

    @Inject
    internal lateinit var addItemUseCase: AddItemUseCase
    @Inject
    internal lateinit var getItemUseCase: GetItemUseCase
    @Inject
    internal lateinit var updateItemUseCase: UpdateItemUseCase

    @FXML
    private lateinit var idTextField: TextField
    @FXML
    private lateinit var nameTextField: TextField
    @FXML
    private lateinit var dimensionTextField: TextField
    @FXML
    private lateinit var descriptionTextField: TextField
    @FXML
    private lateinit var amountTextField: TextField
    @FXML
    private lateinit var unitTextField: TextField
    @FXML
    private lateinit var pricePerUnitTextField: TextField
    @FXML
    private lateinit var cancelButton: Button
    @FXML
    private lateinit var saveButton: Button

    private fun initialize(itemId: Int) {
        logger.info("Initialize for itemId '$itemId'")

        initializeControls(itemId)
        initializeController(itemId)
    }

    private fun initializeControls(itemId: Int) {
        cancelButton.setOnAction { close() }

        idTextField.textFormatter = TextToIntFormatter()
        dimensionTextField.textFormatter = DimensionTextFormatter()
        unitTextField.textFormatter = UnitTextFormatter()
        amountTextField.textFormatter = TextToIntFormatter()
        pricePerUnitTextField.textFormatter = TextToDoubleFormatter()

        idTextField.text = itemId.toString()
    }

    private fun initializeController(itemId: Int) {
        launch(UI) {
            try {
                val item = getItemUseCase.getItem(itemId)
                initializeForUpdate(item)
            } catch (e: UseCaseException) {
                initializeForAdd()
            }
        }
    }

    private fun initializeForAdd() {
        saveButton.setOnAction {
            addItem()
            close()
        }
    }

    private fun initializeForUpdate(item: Item) {
        idTextField.isDisable = true
        nameTextField.isDisable = true
        dimensionTextField.isDisable = true
        descriptionTextField.isDisable = true
        unitTextField.isDisable = true

        fillInMissingFields(item)

        saveButton.setOnAction {
            updateItem()
            close()
        }
    }

    private fun addItem() {
        launch(UI) {
            try {
                addItemUseCase.addNewItem(
                        id = idTextField.text.toInt(),
                        name = nameTextField.text,
                        dimension = dimensionTextField.text,
                        description = descriptionTextField.text,
                        amount = amountTextField.text.toInt(),
                        unit = unitTextField.text,
                        pricePerUnit = pricePerUnitTextField.text.toDouble())
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(ItemErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun updateItem() {
        launch(UI) {
            try {
                updateItemUseCase.updateItem(
                        id = idTextField.text.toInt(),
                        amount = amountTextField.text.toInt(),
                        pricePerUnit = pricePerUnitTextField.text.toDouble())
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(ItemErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun fillInMissingFields(item: Item) {
        nameTextField.text = item.name
        dimensionTextField.text = item.dimension.toString()
        descriptionTextField.text = item.description
        amountTextField.text = item.amount.toString()
        unitTextField.text = item.dimension.unit.toString()
        pricePerUnitTextField.text = item.pricePerUnit.toString()
    }

    private fun close() = (cancelButton.scene.window as Stage).close()
}