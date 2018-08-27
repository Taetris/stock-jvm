package asset.item.manage

import application.StockApplication
import application.executor.UI
import application.usecase.UseCaseException
import asset.item.usecase.AddItemUseCase
import asset.item.usecase.GetItemUseCase
import asset.item.usecase.UpdateItemUseCase
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.item.Item
import util.DialogUtil
import util.TextToDoubleFormatter
import util.TextToIntFormatter
import javax.inject.Inject

class ManageItemController {

    companion object {

        fun create(itemId: Int): Scene {
            val loader = FXMLLoader(ManageItemController::class.java.classLoader.getResource("asset/item/stock-manage-item.fxml"))
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
    private lateinit var parentBox: VBox
    @FXML
    private lateinit var idTextField: TextField
    @FXML
    private lateinit var nameLabel: Label
    @FXML
    private lateinit var nameTextField: TextField
    @FXML
    private lateinit var dimensionLabel: Label
    @FXML
    private lateinit var dimensionTextField: TextField
    @FXML
    private lateinit var descriptionLabel: Label
    @FXML
    private lateinit var descriptionTextField: TextField
    @FXML
    private lateinit var amountTextField: TextField
    @FXML
    private lateinit var unitLabel: Label
    @FXML
    private lateinit var unitTextField: TextField
    @FXML
    private lateinit var pricePerUnitTextField: TextField

    @FXML
    private lateinit var cancelButton: Button
    @FXML
    private lateinit var saveButton: Button

    private fun initialize(id: Int) {
        logger.info("Initialize for id '$id'")

        initializeControls(id)
        initializeController(id)
    }

    private fun initializeController(id: Int) {
        launch(UI) {
            try {
                val item = getItemUseCase.getItem(id)
                initializeForUpdate(item)
            } catch (e: UseCaseException) {
                initializeForAdd()
            }
        }
    }

    private fun initializeControls(id: Int) {
        cancelButton.setOnAction { close() }

        idTextField.textFormatter = TextToIntFormatter()
        dimensionTextField.textFormatter = DimensionTextFormatter()
        unitTextField.textFormatter = UnitTextFormatter()
        amountTextField.textFormatter = TextToIntFormatter()
        pricePerUnitTextField.textFormatter = TextToDoubleFormatter()

        idTextField.text = id.toString()
    }

    private fun initializeForAdd() {
        saveButton.setOnAction { addItem() }
    }

    private fun initializeForUpdate(item: Item) {
        idTextField.isDisable = true

        parentBox.children.remove(nameLabel)
        parentBox.children.remove(nameTextField)
        parentBox.children.remove(dimensionLabel)
        parentBox.children.remove(dimensionTextField)
        parentBox.children.remove(descriptionLabel)
        parentBox.children.remove(descriptionTextField)
        parentBox.children.remove(unitLabel)
        parentBox.children.remove(unitTextField)
        parentBox.prefHeight = saveButton.layoutY
        parentBox.maxHeight = saveButton.layoutY

        fillInFields(item)

        saveButton.setOnAction { updateItem() }
    }

    private fun fillInFields(item: Item) {
        amountTextField.text = item.amount.toString()
        pricePerUnitTextField.text = item.pricePerUnit.toString()
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
                close()
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(header = "Failed to insert item", content = e.message)
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
                DialogUtil.showErrorDialog(header = "Failed to update the item", content = e.message)
            }
        }
    }

    private fun close() {
        (cancelButton.scene.window as Stage).close()
    }
}