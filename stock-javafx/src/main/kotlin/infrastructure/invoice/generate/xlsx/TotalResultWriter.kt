package infrastructure.invoice.generate.xlsx

import application.ResourceLoader
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class TotalResultWriter(private val workbook: XSSFWorkbook, private val sheet: XSSFSheet,
                        private val beginColumn: Int) {

    companion object {

        private const val NUMBER_OF_COLUMNS = 8
    }

    private val cellStyle = createBorderCellStyle()
    private var currentRow: Int = 0

    fun writeTotal(currentRow: Int, totalWithoutTax: Double, totalWithTax: Double): Int {
        this.currentRow = currentRow

        writeTotalPriceWithoutTax(totalWithoutTax.toString())
        increaseRow()
        writeTax(totalWithoutTax, totalWithTax)
        increaseRow()
        writeTotalPriceWithTax(totalWithTax.toString())

        return currentRow
    }

    private fun writeTotalPriceWithoutTax(totalPriceWithoutTax: String) {
        val totalPriceWithoutTaxRow = sheet.createRow(currentRow)
        writeText(totalPriceWithoutTaxRow, "allTotalPriceWithoutTax")
        writeValue(totalPriceWithoutTaxRow, totalPriceWithoutTax)
    }

    private fun writeTax(totalWithoutTax: Double, totalWithTax: Double) {
        val taxRow = sheet.createRow(currentRow)
        writeText(taxRow, "taxDescription")
        writeValue(taxRow, "${round(totalWithTax - totalWithoutTax)}")
    }

    private fun writeTotalPriceWithTax(totalPriceWithTax: String) {
        val totalPriceWithTaxRow = sheet.createRow(currentRow)
        writeText(totalPriceWithTaxRow, "allTotalPriceWithTax")
        writeValue(totalPriceWithTaxRow, totalPriceWithTax)
    }

    private fun writeText(row: XSSFRow, textKey: String) {
        val cell = row.createCell((beginColumn + NUMBER_OF_COLUMNS) - 1)
        cell.cellStyle = cellStyle
        cell.setCellValue(ResourceLoader.bundle.getString(textKey))
        sheet.autoSizeColumn(cell.columnIndex)
    }

    private fun writeValue(row: XSSFRow, value: String) {
        val cell = row.createCell(beginColumn + NUMBER_OF_COLUMNS)
        cell.cellStyle = cellStyle
        cell.setCellValue(value)
        sheet.autoSizeColumn(cell.columnIndex)
    }

    private fun increaseRow() = currentRow++

    private fun round(value: Double): Double {
        val factor = Math.pow(10.0, 2.0)
        return Math.round(value * factor) / factor
    }

    private fun createBorderCellStyle(): XSSFCellStyle {
        val cellStyle = workbook.createCellStyle()
        cellStyle.setAlignment(HorizontalAlignment.CENTER)
        cellStyle.setBorderTop(BorderStyle.THIN)
        cellStyle.setBorderBottom(BorderStyle.THIN)
        cellStyle.setBorderLeft(BorderStyle.THIN)
        cellStyle.setBorderRight(BorderStyle.THIN)
        return cellStyle
    }
}