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
 * Use case for retrieval of a specific customer.
 */
class GetCustomerUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(GetCustomerUseCase::class.java)

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    /**
     * Gets a customer with the given id. In case the customer doesn't exist, or the operation fails, a [UseCaseException]
     * will be thrown.
     *
     * Expected error codes:
     * [ErrorCode.NOT_EXIST] - in case the operation fails due to a missing customer.
     * [ErrorCode.OPERATION_FAILED] - in case the retrieval of a customer fails.
     */
    @Throws(UseCaseException::class)
    suspend fun getCustomer(id: Int) : Customer {
        return withContext(CommonPool) {
            logger.info("Retrieving customer with id '$id")

            if (!customerRepository.containsCustomer(id)) {
                logger.error("Customer with id '$id' doesn't exist.")
                throw UseCaseException(ErrorCode.NOT_EXIST)
            }

            try {
                return@withContext customerRepository.getCustomerById(id)
            } catch (e: RepositoryException) {
                val message = "Failed to get customer with id '$id'. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            }
        }
    }
}