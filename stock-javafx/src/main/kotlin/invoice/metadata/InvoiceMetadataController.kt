package invoice.metadata

import application.ResourceLoader
import application.StockApplication
import application.executor.UI
import application.usecase.ErrorCodeMapper
import application.usecase.UseCaseException
import asset.customer.error.CustomerErrorCodeMapper
import asset.customer.usecase.GetCustomerUseCase
import invoice.items.overview.SelectedItemsOverviewController
import invoice.model.Invoice
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
import view.formatter.TextToIntFormatter
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
    @Inject
    internal lateinit var getCustomerUseCase: GetCustomerUseCase

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

        initializeProperties()
    }

    private fun initializeProperties() {
        invoiceIdTextField.isDisable = true
        invoiceCustomerTextField.textFormatter = TextToIntFormatter()
        invoiceIdTextField.textFormatter = TextToIntFormatter()

        invoiceRetailRadioButton.selectedProperty().addListener { _, _, _ -> updateIdForGroup(InvoiceGroup.RETAIL) }
        invoiceWholesaleRadioButton.selectedProperty().addListener { _, _, _ -> updateIdForGroup(InvoiceGroup.WHOLESALE) }

        invoiceCancelButton.setOnAction { close() }
        invoiceContinueButton.setOnAction { createMetadataAndNavigateToItemSelection() }

        invoiceRetailRadioButton.isSelected = true
    }

    private fun createMetadataAndNavigateToItemSelection() {
        launch(UI) {
            val customerId = invoiceCustomerTextField.text.toInt()
            val invoiceId = invoiceIdTextField.text.toInt()

            try {
                val customer = getCustomerUseCase.getCustomer(customerId)
                val invoice = Invoice(invoiceId, customer)
                navigateToItemSelection(invoice)
            } catch (e: UseCaseException) {
                DialogUtil.showErrorDialog(CustomerErrorCodeMapper.mapErrorCodeToMessage(e.errorCode))
            }
        }
    }

    private fun navigateToItemSelection(invoice: Invoice) {
        val scene = SelectedItemsOverviewController.create(invoice)
        getStage().scene = scene
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
        getStage().close()
    }

    private fun getStage(): Stage = invoiceCancelButton.scene.window as Stage
}