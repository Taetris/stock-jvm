package invoice.usecase

import application.usecase.ErrorCode
import application.usecase.UseCaseException
import infrastructure.invoice.generate.xlsx.XlsxInvoiceOutputGenerator
import invoice.model.Invoice
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.customer.Customer
import java.io.File
import javax.inject.Inject

class GenerateInvoiceUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(GenerateInvoiceUseCase::class.java)

    private val xslxInvoiceOutputGenerator = XlsxInvoiceOutputGenerator()

    @Throws(UseCaseException::class)
    suspend fun generateInvoice(invoice: Invoice, output: File) {
        withContext(CommonPool) {
            logger.info("Generating invoice")

            try {
                val retailer = Customer(1, "Euro-Itex d.o.o", "4200261890007", "200261890007", "Ulica Zuhdije Zalica bb")
                xslxInvoiceOutputGenerator.generate(invoice, invoice.customer, retailer, output)
            } catch (e: RuntimeException) {
                val message = "Failed to generate invoice. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            }
        }
    }
}
