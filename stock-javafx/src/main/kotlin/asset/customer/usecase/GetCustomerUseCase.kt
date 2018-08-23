package asset.customer.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.customer.Customer
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject

class GetCustomerUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(GetCustomerUseCase::class.java)

    @Inject
    lateinit var customerRepository: ObservableCustomerRepository

    @Throws(UseCaseException::class)
    suspend fun getCustomer(id: Int) : Customer {
        return withContext(CommonPool) {
            logger.info("Retrieving customer with id '$id")

            try {
                return@withContext customerRepository.getCustomerById(id)
            } catch (e: RepositoryException) {
                val message = "Failed to get customer with id '$id'."
                logger.info(message, e)
                throw UseCaseException(message, e)
            }
        }
    }
}