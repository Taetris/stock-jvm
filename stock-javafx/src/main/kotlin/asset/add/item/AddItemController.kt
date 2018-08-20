package asset.add.item

import asset.add.item.interactor.AddItemInteractor
import asset.add.item.interactor.AddItemOutput
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import util.Dialog
import util.NumberTextFormatter

class AddItemController : AddItemOutput {

    companion object {

        fun create(): Scene {
            val view = FXMLLoader.load<Pane>(AddItemController::class.java.getResource("../../../../resources/asset/add/stock-add-item.fxml"))
            return Scene(view)
        }
    }

    private val logger = LoggerFactory.getLogger(AddItemController::class.java)

    @FXML
    private lateinit var cancelButton: Button
    @FXML
    private lateinit var addButton: Button
    @FXML
    private lateinit var idTextField: TextField
    @FXML
    private lateinit var nameTextField: TextField
    @FXML
    private lateinit var accountNumberTextField: TextField
    @FXML
    private lateinit var addressTextField: TextField

    private val interactor = AddItemInteractor(this)

    @FXML
    fun initialize() {
        logger.info("Initialize")

        setListeners()
    }

    override fun onInsertionSuccessful() {
        (cancelButton.scene.window as Stage).close()
    }

    override fun onInsertionFailed(error: String) {
        Dialog.showErrorDialog(header = "Failed to insert item", content = error)
    }

    private fun setListeners() {
        idTextField.textFormatter = NumberTextFormatter()

        cancelButton.setOnAction { (cancelButton.scene.window as Stage).close() }
        addButton.setOnAction {
            interactor.addNewItem(
                    idTextField.text.toInt(),
                    nameTextField.text,
                    accountNumberTextField.text,
                    addressTextField.text)
        }
    }
}