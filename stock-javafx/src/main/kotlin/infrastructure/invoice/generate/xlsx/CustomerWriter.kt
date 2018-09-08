package infrastructure.invoice.generate.xlsx

import application.ResourceLoader
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import repository.customer.Customer

class CustomerWriter(private val workbook: XSSFWorkbook, private val sheet: XSSFSheet, private val beginColumn: Int) {

    private var currentRow: Int = 0

    fun writeCustomer(currentRow: Int, customer: Customer): Int {
        this.currentRow = currentRow

        formatAndWriteName(customer.name)
        formatAddress(customer.address)
        formatIdNumber(customer.idNumber)
        formatPdvNumber(customer.pdvNumber)

        return this.currentRow
    }

    private fun formatAndWriteName(name: String) {
        currentRow = writeValueAndReturnNewRow(currentRow, name)
    }

    private fun formatAddress(address: String) {
        val formattedAddress = formatValue("customerAddress", address)
        currentRow = writeValueAndReturnNewRow(currentRow, formattedAddress)
    }

    private fun formatIdNumber(idNumber: String) {
        val formattedIdNumber = formatValue("customerIdNumber", idNumber)
        currentRow = writeValueAndReturnNewRow(currentRow, formattedIdNumber)
    }

    private fun formatPdvNumber(pdvNumber: String) {
        val formattedPdvNumber = formatValue("customerPdvNumber", pdvNumber)
        currentRow = writeValueAndReturnNewRow(currentRow, formattedPdvNumber)
    }

    private fun formatValue(descriptionKey: String, value: String): String {
        val text = ResourceLoader.bundle.getString(descriptionKey).toUpperCase()
        return "$text: $value"
    }

    private fun writeValueAndReturnNewRow(currentRow: Int, value: String): Int {
        val row = sheet.getRow(currentRow) ?: sheet.createRow(currentRow)
        val cell = row.createCell(beginColumn)
        cell.setCellValue(value)

        return currentRow + 1
    }
}