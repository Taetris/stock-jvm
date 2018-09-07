package menu

import application.StockApplication
import application.executor.UI
import asset.customer.usecase.GetCustomerUseCase
import asset.item.usecase.GetItemUseCase
import infrastructure.invoice.generate.xslx.XlsxInvoiceOutputGenerator
import invoice.model.Invoice
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MenuController {

    init {
        StockApplication.stockComponent.inject(this)
    }

    private val logger = LoggerFactory.getLogger(MenuController::class.java)

    @FXML
    private lateinit var createInvoiceMenuItem: MenuItem
    @FXML
    private lateinit var exitMenuItem: MenuItem

    @Inject
    lateinit var getCustomerUseCase: GetCustomerUseCase
    @Inject
    lateinit var getItemUseCase: GetItemUseCase

    @FXML
    fun initialize() {
        logger.info("Initialize")

        createInvoiceMenuItem.setOnAction { createInvoice() }
        exitMenuItem.setOnAction { close() }
    }

    private fun createInvoice() {
//        val stage = Stage()
//        stage.scene = InvoiceMetadataController.create()
//        stage.show()

        launch(UI) {
            val customer = getCustomerUseCase.getCustomer(1)
            val invoice = Invoice(1, customer)
            invoice.selectedItems.add(getItemUseCase.getItem(1))
            invoice.selectedItems.add(getItemUseCase.getItem(2))
            invoice.selectedItems.add(getItemUseCase.getItem(3))
            invoice.selectedItems.add(getItemUseCase.getItem(4))
            invoice.selectedItems.add(getItemUseCase.getItem(5))

            XlsxInvoiceOutputGenerator().generate(invoice, customer, "E:\\Dev\\stock-jvm\\stock-javafx\\build\\test.xlsx")
        }

    }

    private fun close() {
        Platform.exit()
        System.exit(0)
    }
}