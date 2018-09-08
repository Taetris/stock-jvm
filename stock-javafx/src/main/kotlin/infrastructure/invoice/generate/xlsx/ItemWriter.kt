package infrastructure.invoice.generate.xlsx

import application.ResourceLoader
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import repository.item.Item

class ItemWriter(private val workbook: XSSFWorkbook, private val sheet: XSSFSheet,
                 private val beginColumn: Int) {

    companion object {

        private const val NUMBER_OF_COLUMNS = 8
    }

    private val ID_COLUMN = beginColumn
    private val NAME_COLUMN = beginColumn + NUMBER_OF_COLUMNS - 7
    private val DIMENSION_COLUMN = beginColumn + NUMBER_OF_COLUMNS - 6
    private val DESCRIPTION_COLUMN = beginColumn + NUMBER_OF_COLUMNS - 5
    private val AMOUNT_COLUMN = beginColumn + NUMBER_OF_COLUMNS - 4
    private val UNIT_COLUMN = beginColumn + NUMBER_OF_COLUMNS - 3
    private val QUANTITY_COLUMN = beginColumn + NUMBER_OF_COLUMNS - 2
    private val PRICE_PER_UNIT_COLUMN = beginColumn + NUMBER_OF_COLUMNS - 1
    private val TOTAL_PRICE_WITHOUT_TAX_COLUMN = beginColumn + NUMBER_OF_COLUMNS

    private val cellStyle = createBorderCellStyle()

    fun writeItemHeader(currentRow: Int): Int {
        val row = sheet.createRow(currentRow)

        for (i in beginColumn..(beginColumn + NUMBER_OF_COLUMNS)) {
            print("$i")

            val cell = row.createCell(i)
            cell.cellStyle = cellStyle
            cell.setCellValue(getColumnNameForIndex(i))
            sheet.autoSizeColumn(cell.columnIndex)
        }

        return currentRow + 1
    }

    fun writeItem(currentRow: Int, item: Item): Int {
        val row = sheet.createRow(currentRow)

        for (i in beginColumn..(beginColumn + NUMBER_OF_COLUMNS)) {
            val cell = row.createCell(i)
            cell.cellStyle = cellStyle
            cell.setCellValue(getColumnValueForIndex(i, item))
            sheet.autoSizeColumn(cell.columnIndex)
        }

        return currentRow + 1
    }

    private fun getColumnNameForIndex(columnIndex: Int): String {
        return ResourceLoader.bundle.getString(when (columnIndex) {
            ID_COLUMN -> "itemId"
            NAME_COLUMN -> "itemName"
            DIMENSION_COLUMN -> "itemDimension"
            DESCRIPTION_COLUMN -> "itemDescription"
            AMOUNT_COLUMN -> "itemAmount"
            UNIT_COLUMN -> "itemUnit"
            QUANTITY_COLUMN -> "itemQuantity"
            PRICE_PER_UNIT_COLUMN -> "itemPricePerUnit"
            TOTAL_PRICE_WITHOUT_TAX_COLUMN -> "itemTotalPriceWithoutTax"
            else -> throw IllegalArgumentException("Unexpected columnIndex '$columnIndex'")
        }).toUpperCase()
    }

    private fun getColumnValueForIndex(columnIndex: Int, item: Item): String {
        return when (columnIndex) {
            ID_COLUMN -> item.id.toString()
            NAME_COLUMN -> item.name
            DIMENSION_COLUMN -> item.dimension.toString()
            DESCRIPTION_COLUMN -> item.description
            AMOUNT_COLUMN -> item.amount.toString()
            UNIT_COLUMN -> item.dimension.unit.value
            QUANTITY_COLUMN -> item.calculateQuantity().toString()
            PRICE_PER_UNIT_COLUMN -> item.pricePerUnit.toString()
            TOTAL_PRICE_WITHOUT_TAX_COLUMN -> item.calculateTotalPriceWithoutTax().toString()
            else -> throw IllegalArgumentException("Unexpected columnIndex '$columnIndex'")
        }
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