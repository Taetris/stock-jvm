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
 * Use case for a removal of a customer.
 */
class RemoveCustomerUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(RemoveCustomerUseCase::class.java)

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    /**
     * Removes a customer from the repository. If the customer doesn't exist, or the removal fails,
     * a [UseCaseException] will be thrown.
     *
     * Expected error codes:
     * [ErrorCode.NOT_EXIST] - in case the operation fails due to a missing customer.
     * [ErrorCode.OPERATION_FAILED] - in case the removal failed.
     */
    @Throws(UseCaseException::class)
    suspend fun removeCustomer(customer: Customer) {
        withContext(CommonPool) {
            logger.info("Removing customer with id '${customer.id}")

            if (!customerRepository.containsCustomer(customer.id)) {
                logger.error("Customer with id '${customer.id}' doesn't exist.")
                throw UseCaseException(ErrorCode.NOT_EXIST)
            }

            try {
                customerRepository.removeCustomer(customer)
            } catch (e: RepositoryException) {
                val message = "Failed to remove the customer with id '${customer.id}. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            }
        }
    }
}