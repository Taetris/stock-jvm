package invoice.metadata

import application.ResourceLoader
import application.StockApplication
import application.executor.UI
import application.usecase.ErrorCodeMapper
import application.usecase.UseCaseException
import invoice.model.InvoiceGroup
import invoice.usecase.GetNextInvoiceIdUseCase
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.stage.Stage
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import view.dialog.DialogUtil
import javax.inject.Inject

class InvoiceMetadataController {

    companion object {

        fun create(): Scene {
            val view = ResourceLoader.load(InvoiceMetadataController::class.java, "invoice/stock-invoice-metadata.fxml")
            return Scene(view)
        }
    }

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(InvoiceMetadataController::class.java)

    @Inject
    internal lateinit var getNextInvoiceIdUseCase: GetNextInvoiceIdUseCase

    @FXML
    private lateinit var invoiceIdTextField: TextField
    @FXML
    private lateinit var invoiceCustomerTextField: TextField
    @FXML
    private lateinit var invoiceRetailRadioButton: RadioButton
    @FXML
    private lateinit var invoiceWholesaleRadioButton: RadioButton
    @FXML
    private lateinit var invoiceContinueButton: Button
    @FXML
    private lateinit var invoiceCancelButton: Button

    @FXML
    fun initialize() {
        logger.info("Initialize")

        invoiceIdTextField.isDisable = true

        invoiceRetailRadioButton.setOnAction { updateIdForGroup(InvoiceGroup.RETAIL) }
        invoiceWholesaleRadioButton.setOnAction { updateIdForGroup(InvoiceGroup.WHOLESALE) }
        invoiceCancelButton.setOnAction { close() }
        invoiceContinueButton.setOnAction { close() }
    }

    private fun updateIdForGroup(invoiceGroup: InvoiceGroup) {
        launch(UI) {
            try {
                val id = getNextInvoiceIdUseCase.getNextInvoiceIdForGroup(invoiceGroup)
                invoiceIdTextField.text = id.toString()
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(ErrorCodeMapper().mapErrorCodeToMessage(e.errorCode))
                close()
            }
        }
    }

    private fun close() {
        (invoiceCancelButton.scene.window as Stage).close()
    }
}