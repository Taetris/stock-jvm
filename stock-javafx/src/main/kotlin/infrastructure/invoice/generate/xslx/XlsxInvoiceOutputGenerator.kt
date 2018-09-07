package infrastructure.invoice.generate.xslx

import application.ResourceLoader
import invoice.model.Invoice
import javafx.collections.ObservableList
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import repository.customer.Customer
import repository.item.Item
import java.io.File
import java.io.FileOutputStream

class XlsxInvoiceOutputGenerator {

    companion object {

        private const val TAX = 17.0

        private const val CUSTOMER_BEGIN_COLUMN = 10

        private const val ITEM_BEGIN_COLUMN = 2
        private const val ITEM_COLUMN_END = 10

        private const val START_ROW = 1
    }

    private val workbook = XSSFWorkbook()
    private val sheet = workbook.createSheet()

    private var currentRow = START_ROW

    fun generate(invoice: Invoice, customer: Customer, path: String) {
        val headerWriter = HeaderWriter(sheet)
        val customerWriter = CustomerWriter(workbook, sheet, CUSTOMER_BEGIN_COLUMN)

        headerWriter.writeHeader(invoice)

        currentRow = customerWriter.writeCustomer(currentRow, customer)
        skipRow()
        currentRow = writeItems(currentRow, invoice.selectedItems)

        (FileOutputStream(File(path))).use { workbook.write(it) }
    }

    private fun writeItems(currentRow: Int, items: ObservableList<Item>): Int {
        val itemWriter = ItemWriter(workbook, sheet, ITEM_BEGIN_COLUMN)
        var newRow = currentRow

        newRow = itemWriter.writeItemHeader(newRow)

        items.forEach { newRow = itemWriter.writeItem(newRow, it) }

        return newRow
    }

    private fun writeTotal(items: ObservableList<Item>, rowNumber: Int) {
        var currentRowNumber = rowNumber
        val cellStyle = createBorderCellStyle()

        val totalWithoutTax = round(items.stream().mapToDouble { value -> value.calculateTotalPriceWithoutTax() }.sum())
        val totalWithTax = round(items.stream().mapToDouble { value -> value.calculateTotalPriceWithTax(TAX) }.sum())

        val totalPriceWithoutTaxRow = sheet.createRow(currentRowNumber)
        val totalPriceWithoutTaxDescriptionCell = totalPriceWithoutTaxRow.createCell(ITEM_COLUMN_END - 1)
        totalPriceWithoutTaxDescriptionCell.cellStyle = cellStyle
        totalPriceWithoutTaxDescriptionCell.setCellValue(ResourceLoader.bundle.getString("allTotalPriceWithoutTax"))

        val totalPriceWithoutTaxValueCell = totalPriceWithoutTaxRow.createCell(ITEM_COLUMN_END)
        totalPriceWithoutTaxValueCell.cellStyle = cellStyle
        totalPriceWithoutTaxValueCell.setCellValue(totalWithoutTax.toString())

        currentRowNumber++

        val taxRow = sheet.createRow(currentRowNumber)
        val taxDescriptionCell = taxRow.createCell(ITEM_COLUMN_END - 1)
        taxDescriptionCell.cellStyle = cellStyle
        taxDescriptionCell.setCellValue("${ResourceLoader.bundle.getString("taxDescription")} ${TAX.toInt()}%")

        val taxValueCell = taxRow.createCell(ITEM_COLUMN_END)
        taxValueCell.cellStyle = cellStyle
        taxValueCell.setCellValue("${round(totalWithTax - totalWithoutTax)}")

        currentRowNumber++

        val totalPriceWithTaxRow = sheet.createRow(currentRowNumber)
        val totalPriceWithTaxDescriptionCell = totalPriceWithTaxRow.createCell(ITEM_COLUMN_END - 1)
        totalPriceWithTaxDescriptionCell.cellStyle = cellStyle
        totalPriceWithTaxDescriptionCell.setCellValue(ResourceLoader.bundle.getString("allTotalPriceWithTax"))

        val totalPriceWithTaxValueCell = totalPriceWithTaxRow.createCell(ITEM_COLUMN_END)
        totalPriceWithTaxValueCell.cellStyle = cellStyle
        totalPriceWithTaxValueCell.setCellValue(totalWithTax.toString())
    }

    private fun createBorderCellStyle(): XSSFCellStyle? {
        val cellStyle = workbook.createCellStyle()
        cellStyle.setBorderTop(BorderStyle.THIN)
        cellStyle.setBorderBottom(BorderStyle.THIN)
        cellStyle.setBorderLeft(BorderStyle.THIN)
        cellStyle.setBorderRight(BorderStyle.THIN)
        return cellStyle
    }

    private fun round(value: Double): Double {
        val factor = Math.pow(10.0, 2.0)
        return Math.round(value * factor) / factor
    }

    private fun skipRow() {
        currentRow += 1
    }
}