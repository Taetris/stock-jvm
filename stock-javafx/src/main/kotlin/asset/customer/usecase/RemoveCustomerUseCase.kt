package asset.customer.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.customer.Customer
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject

class RemoveCustomerUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(RemoveCustomerUseCase::class.java)

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    @Throws(UseCaseException::class)
    suspend fun removeCustomer(customer: Customer) {
        withContext(CommonPool) {
            logger.info("Removing customer with id '${customer.id}")

            try {
                customerRepository.removeCustomer(customer)
            } catch (e: RepositoryException) {
                val message = "Failed to remove the customer with id '${customer.id}."
                logger.info(message, e)
                throw UseCaseException(message, e)
            }
        }
    }
}