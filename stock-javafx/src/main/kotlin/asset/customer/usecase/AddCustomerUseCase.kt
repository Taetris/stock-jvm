package asset.customer.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.customer.Customer
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject

class AddCustomerUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(AddCustomerUseCase::class.java)

    @Inject
    lateinit var customerRepository: ObservableCustomerRepository

    @Throws(UseCaseException::class)
    suspend fun addNewCustomer(id: Int, name: String, accountNumber: String, address: String) {
        withContext(CommonPool) {
            val customer = Customer(id, name, accountNumber, address)
            logger.info("Adding new asset.customer '$customer'")

            try {
                customerRepository.insertCustomer(customer)
            } catch (e: RepositoryException) {
                val message = "Failed to add a new asset.customer."
                logger.info(message, e)
                throw UseCaseException(message, e)
            }
        }
    }
}