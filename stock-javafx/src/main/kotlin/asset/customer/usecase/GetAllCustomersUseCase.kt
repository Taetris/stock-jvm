package asset.customer.usecase

import application.usecase.ErrorCode
import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.customer.Customer
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject

/**
 * Use case for the retrieval of all customers.
 */
class GetAllCustomersUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(GetAllCustomersUseCase::class.java)

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    /**
     * Gets all customers from the repository. In case the operation fails, a [UseCaseException] will be thrown.
     *
     * Expected error codes:
     * [ErrorCode.OPERATION_FAILED] - in case the retrieval of customers failed.
     */
    @Throws(UseCaseException::class)
    suspend fun getAllCustomers(): List<Customer> {
        return withContext(CommonPool) {
            logger.info("Getting all customers")

            try {
                return@withContext customerRepository.getAllCustomers()
            } catch (e: RepositoryException) {
                val message = "Failed to retrieve all customers. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            }
        }
    }
}