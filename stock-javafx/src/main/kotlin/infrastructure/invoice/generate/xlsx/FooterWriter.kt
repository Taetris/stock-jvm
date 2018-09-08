package infrastructure.invoice.generate.xlsx

import application.ResourceLoader
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet

class FooterWriter(private val sheet: XSSFSheet, private val beginColumn: Int) {

    fun writeFooter(currentRow: Int): Int {
        var row = currentRow

        val signatureTypeRow = sheet.createRow(row)
        writeSignatureType(signatureTypeRow, beginColumn, "shippingSignature")
        writeSignatureType(signatureTypeRow, beginColumn + 4, "driverSignature")
        writeSignatureType(signatureTypeRow, beginColumn + 8, "receivingSignature")

        row++

        val signatureLineRow = sheet.createRow(row)
        writeSignatureLine(signatureLineRow, beginColumn)
        writeSignatureLine(signatureLineRow, beginColumn + 4)
        writeSignatureLine(signatureLineRow, beginColumn + 8)

        return row
    }

    private fun writeSignatureType(row: XSSFRow, columnNumber: Int, signatureType: String) {
        val cell = row.createCell(columnNumber)
        cell.setCellValue(ResourceLoader.bundle.getString(signatureType))
    }

    private fun writeSignatureLine(row: XSSFRow, columnNumber: Int) {
        val cell = row.createCell(columnNumber)
        cell.setCellValue(ResourceLoader.bundle.getString("signatureLine"))
    }
}