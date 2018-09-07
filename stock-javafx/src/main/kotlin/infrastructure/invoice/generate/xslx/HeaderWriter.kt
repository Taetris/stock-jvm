package infrastructure.invoice.generate.xslx

import application.ResourceLoader
import invoice.model.Invoice
import org.apache.poi.xssf.usermodel.XSSFSheet

class HeaderWriter(private val sheet: XSSFSheet) {

        fun writeHeader(invoice: Invoice) {
        val invoiceText = ResourceLoader.bundle.getString("invoiceHeader")
        val formattedInvoiceId = formatInvoiceId(invoice)
        val formattedDate = formatDate(invoice)

        write("$invoiceText $formattedInvoiceId\n$formattedDate")
    }

    private fun write(value: String) {
        sheet.header.center = value
    }

    private fun formatInvoiceId(invoice: Invoice): String {
        val id = invoice.invoiceId
        val year = invoice.localDate.year

        return "$id/$year"
    }

    private fun formatDate(invoice: Invoice): String {
        val date = invoice.localDate
        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        return "$day.$month.$year"
    }
}