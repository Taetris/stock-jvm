package infrastructure.invoice.generate.xslx

import application.ResourceLoader
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import repository.customer.Customer

class CustomerWriter(private val workbook: XSSFWorkbook, private val sheet: XSSFSheet, private val beginColumn: Int) {

    private val topLeftRightBorder = createBorderCellStyle(true, true, false, true)
    private val leftRightBorder = createBorderCellStyle(false, true, false, true)
    private val leftBottomRightBorder = createBorderCellStyle(false, true, true, true)

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
        val formattedName = formatValue("customerName", name)
        currentRow = writeValueAndReturnNewRow(currentRow, topLeftRightBorder, formattedName)
    }

    private fun formatAddress(address: String) {
        val formattedAddress = formatValue("customerAddress", address)
        currentRow = writeValueAndReturnNewRow(currentRow, leftRightBorder, formattedAddress)
    }

    private fun formatIdNumber(idNumber: String) {
        val formattedIdNumber = formatValue("customerIdNumber", idNumber)
        currentRow = writeValueAndReturnNewRow(currentRow, leftRightBorder, formattedIdNumber)
    }

    private fun formatPdvNumber(pdvNumber: String) {
        val formattedPdvNumber = formatValue("customerPdvNumber", pdvNumber)
        currentRow = writeValueAndReturnNewRow(currentRow, leftBottomRightBorder, formattedPdvNumber)
    }

    private fun formatValue(descriptionKey: String, value: String): String {
        val text = ResourceLoader.bundle.getString(descriptionKey).toUpperCase()
        return "$text: $value"
    }

    private fun writeValueAndReturnNewRow(currentRow: Int, cellStyle: XSSFCellStyle, value: String): Int {
        val row = sheet.createRow(currentRow)
        val cell = row.createCell(beginColumn)
        cell.cellStyle = cellStyle
        cell.setCellValue(value)

        return currentRow + 1
    }

    private fun createBorderCellStyle(top: Boolean, left: Boolean, bottom: Boolean, right: Boolean): XSSFCellStyle {
        val cellStyle = workbook.createCellStyle()
        if (top) cellStyle.setBorderTop(BorderStyle.THIN)
        if (bottom) cellStyle.setBorderBottom(BorderStyle.THIN)
        if (left) cellStyle.setBorderLeft(BorderStyle.THIN)
        if (right) cellStyle.setBorderRight(BorderStyle.THIN)
        return cellStyle
    }
}