package invoice.usecase

import application.usecase.ErrorCode
import application.usecase.UseCaseException
import invoice.model.InvoiceGroup
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.invoice.InvoiceRepository
import javax.inject.Inject

/**
 * Use case for the retrieval of next valid invoice id for a specific group.
 */
class GetNextInvoiceIdUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(GetNextInvoiceIdUseCase::class.java)

    @Inject
    internal lateinit var invoiceRepository: InvoiceRepository

    /**
     * Gets the next valid invoice id for the given group. If the retrieval failed, a [UseCaseException] will be thrown.
     */
    @Throws(UseCaseException::class)
    suspend fun getNextInvoiceIdForGroup(invoiceGroup: InvoiceGroup): Int {
        return withContext(CommonPool) {
            logger.info("Getting invoice id for group '$invoiceGroup'")

            try {
                return@withContext when (invoiceGroup) {
                    InvoiceGroup.RETAIL -> invoiceRepository.getNextInvoiceIdForRetail()
                    InvoiceGroup.WHOLESALE -> invoiceRepository.getNextInvoiceIdForWholesale()
                }
            } catch (e: RepositoryException) {
                val message = "Failed to retrieve id for group '$invoiceGroup'. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            }
        }
    }
}