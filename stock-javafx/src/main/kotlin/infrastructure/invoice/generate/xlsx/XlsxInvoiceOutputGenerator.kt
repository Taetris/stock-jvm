package infrastructure.invoice.generate.xlsx

import invoice.model.Invoice
import javafx.collections.ObservableList
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import repository.customer.Customer
import repository.item.Item
import java.io.File
import java.io.FileOutputStream


class XlsxInvoiceOutputGenerator {

    companion object {

        private const val TAX = 17.0

        private const val RETAILER_BEGIN_COLUMN = 1
        private const val CUSTOMER_BEGIN_COLUMN = 10

        private const val ITEM_BEGIN_COLUMN = 2
        private const val FOOTER_BEGIN_COLUMN = 2

        private const val START_ROW = 1
    }

    private val workbook = XSSFWorkbook()
    private val sheet = workbook.createSheet()

    private val headerWriter = HeaderWriter(sheet)
    private val customerWriter = CustomerWriter(workbook, sheet, CUSTOMER_BEGIN_COLUMN)
    private val retailerWriter = CustomerWriter(workbook, sheet, RETAILER_BEGIN_COLUMN)
    private val itemWriter = ItemWriter(workbook, sheet, ITEM_BEGIN_COLUMN)
    private val totalResultWriter = TotalResultWriter(workbook, sheet, ITEM_BEGIN_COLUMN)
    private val footerWriter = FooterWriter(sheet, FOOTER_BEGIN_COLUMN)

    private var currentRow = START_ROW

    //TODO AA: Fix hardcoded retailer
    fun generate(invoice: Invoice, customer: Customer, retailer: Customer, output: File) {
        headerWriter.writeHeader(invoice)

        retailerWriter.writeCustomer(currentRow, retailer)
        currentRow = customerWriter.writeCustomer(currentRow, customer)
        skipRows(1)
        currentRow = writeItems(invoice.selectedItems)
        currentRow = writeTotal(invoice.selectedItems)
        skipRows(6)
        footerWriter.writeFooter(currentRow)

        (FileOutputStream(output)).use { workbook.write(it) }
    }

    private fun writeItems(items: ObservableList<Item>): Int {
        currentRow = itemWriter.writeItemHeader(currentRow)
        items.forEach { currentRow = itemWriter.writeItem(currentRow, it) }
        return currentRow
    }

    private fun writeTotal(items: ObservableList<Item>): Int {
        val totalWithoutTax = round(items.stream().mapToDouble { value -> value.calculateTotalPriceWithoutTax() }.sum())
        val totalWithTax = round(items.stream().mapToDouble { value -> value.calculateTotalPriceWithTax(TAX) }.sum())

        return totalResultWriter.writeTotal(currentRow, totalWithoutTax, totalWithTax)
    }

    private fun round(value: Double): Double {
        val factor = Math.pow(10.0, 2.0)
        return Math.round(value * factor) / factor
    }

    private fun skipRows(n: Int) {
        currentRow += n
    }
}