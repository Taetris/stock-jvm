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
 * Use case for updating of a customer.
 */
class UpdateCustomerUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(UpdateCustomerUseCase::class.java)

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    /**
     * Updates the customer with the same id. If the customer doesn't exist, or the update fails,
     * a [UseCaseException] will be thrown.
     *
     * Expected error codes:
     * [ErrorCode.NOT_EXIST] - in case the operation fails due to a missing customer.
     * [ErrorCode.OPERATION_FAILED] - in case the update failed.
     */
    @Throws(UseCaseException::class)
    suspend fun updateCustomer(id: Int, name: String, idNumber: String, pdvNumber: String, address: String) {
        withContext(CommonPool) {
            logger.info("Updating customer with the id: '$id'")

            if (!customerRepository.containsCustomer(id)) {
                logger.error("Customer with id '$id' doesn't exist.")
                throw UseCaseException(ErrorCode.NOT_EXIST)
            }


            try {
                val customer = Customer(id, name, idNumber, pdvNumber, address)
                logger.info("Updating the customer with the id '${customer.id} with data '$customer'")
                customerRepository.updateCustomer(customer)
            } catch (e: RepositoryException) {
                val message = "Failed to update the customer with id '$id. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            } catch (e: IllegalArgumentException) {
                val message = "Invalid input parameters. Error: '${e.message}."
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.INVALID_INPUT)
            }
        }
    }
}