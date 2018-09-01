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
 * Use case for insertion of a new customer.
 */
class AddCustomerUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(AddCustomerUseCase::class.java)

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    /**
     * Inserts a new customer into the repository. If the customer already exists, or the insertion failed,
     * a [UseCaseException] will be thrown.
     *
     * Expected error codes:
     * [ErrorCode.ALREADY_EXISTS] - in case the operation fails due to an already existing customer.
     * [ErrorCode.OPERATION_FAILED] - in case the insertion failed.
     */
    @Throws(UseCaseException::class)
    suspend fun addNewCustomer(id: Int, name: String, idNumber: String, pdvNumber: String, address: String) {
        withContext(CommonPool) {
            logger.info("Adding a new customer with id '$id'")

            if (customerRepository.containsCustomer(id)) {
                logger.error("Customer with id '$id' already exists.")
                throw UseCaseException(ErrorCode.ALREADY_EXISTS)
            }


            try {
                val customer = Customer(id, name, idNumber, pdvNumber, address)
                logger.info("Adding a new customer with data '$customer'")
                customerRepository.insertCustomer(customer)
            } catch (e: RepositoryException) {
                val message = "Failed to add a new customer. Error: '${e.message}"
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