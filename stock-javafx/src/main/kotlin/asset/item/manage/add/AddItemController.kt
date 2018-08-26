package asset.item.manage.add

import application.StockApplication
import application.executor.UI
import application.usecase.UseCaseException
import asset.item.usecase.AddItemUseCase
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.stage.Stage
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import util.DialogUtil
import util.TextToDoubleFormatter
import util.TextToIntFormatter
import javax.inject.Inject

class AddItemController {

    companion object {

        fun create(): Scene {
            val view = FXMLLoader.load<Pane>(AddItemController::class.java.getResource("../../../../../resources/asset/item/stock-add-item.fxml"))
            return Scene(view)
        }
    }

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(AddItemController::class.java)

    @Inject
    internal lateinit var addItemUseCase: AddItemUseCase

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
    private lateinit var addButton: Button

    @FXML
    fun initialize() {
        logger.info("Initialize")

        initializeControls()
    }

    private fun initializeControls() {
        cancelButton.setOnAction { close() }
        addButton.setOnAction { addItem() }

        idTextField.textFormatter = TextToIntFormatter()
        dimensionTextField.textFormatter = DimensionTextFormatter()
        unitTextField.textFormatter = UnitTextFormatter()
        amountTextField.textFormatter = TextToIntFormatter()
        pricePerUnitTextField.textFormatter = TextToDoubleFormatter()
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

    private fun close() {
        (cancelButton.scene.window as Stage).close()
    }
}