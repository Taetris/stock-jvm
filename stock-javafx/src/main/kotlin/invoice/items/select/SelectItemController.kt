package invoice.items.select

import application.ResourceLoader
import application.StockApplication
import asset.item.manage.ManageItemController
import invoice.model.Invoice
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import repository.item.Item
import view.formatter.TextToDoubleFormatter
import view.formatter.TextToIntFormatter

class SelectItemController {

    companion object {

        const val MIN_AMOUNT = 1

        fun create(invoice: Invoice, item: Item): Scene {
            val loader = ResourceLoader.loader(SelectItemController::class.java, "invoice/stock-invoice-select-item.fxml")
            val view = loader.load<Pane>()
            val controller = loader.getController<SelectItemController>()
            controller.initialize(invoice, item)
            return Scene(view)
        }
    }

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(ManageItemController::class.java)

    @FXML
    private lateinit var idTextField: TextField
    @FXML
    private lateinit var amountSpinner: Spinner<Int>
    @FXML
    private lateinit var pricePerUnitTextField: TextField
    @FXML
    private lateinit var cancelButton: Button
    @FXML
    private lateinit var saveButton: Button

    private lateinit var invoice: Invoice
    private lateinit var item: Item

    private fun initialize(invoice: Invoice, item: Item) {
        logger.info("Initialize for invoice '$invoice' and item '$item'")

        this.invoice = invoice
        this.item = item

        initializeControls()
    }

    private fun initializeControls() {
        idTextField.textFormatter = TextToIntFormatter()
        amountSpinner.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_AMOUNT, item.amount)
        pricePerUnitTextField.textFormatter = TextToDoubleFormatter()

        fillInFields()

        cancelButton.setOnAction { close() }
        saveButton.setOnAction {
            addItem()
            close()
        }
    }

    private fun fillInFields() {
        idTextField.text = item.id.toString()
        pricePerUnitTextField.text = item.pricePerUnit.toString()
    }

    private fun addItem() {
        val copiedItem = item.copy(
                amount = amountSpinner.value,
                pricePerUnit = pricePerUnitTextField.text.toDouble())
        invoice.selectedItems.add(copiedItem)
    }

    private fun close() = (cancelButton.scene.window as Stage).close()
}