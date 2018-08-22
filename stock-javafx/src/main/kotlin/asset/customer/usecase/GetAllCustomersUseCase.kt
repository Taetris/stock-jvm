package asset.customer.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.customer.Customer
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject

class GetAllCustomersUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(GetAllCustomersUseCase::class.java)

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    @Throws(UseCaseException::class)
    suspend fun getAllCustomers(): List<Customer> {
        return withContext(CommonPool) {
            logger.info("Getting all customers")

            try {
                return@withContext customerRepository.getAllCustomers()
            } catch (e: RepositoryException) {
                val message = "Failed to retrieve all customers."
                logger.info(message, e)
                throw UseCaseException(message, e)
            }
        }
    }
}