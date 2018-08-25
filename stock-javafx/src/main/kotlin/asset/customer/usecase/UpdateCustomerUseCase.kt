package asset.customer.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.customer.Customer
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject

class UpdateCustomerUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(UpdateCustomerUseCase::class.java)

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    @Throws(UseCaseException::class)
    suspend fun updateCustomer(id: Int, name: String, idNumber: String, pdvNumber: String, address: String) {
        withContext(CommonPool) {
            val customer = Customer(id, name, idNumber, pdvNumber, address)
            logger.info("Updating the customer with the id '${customer.id} with data '$customer'")

            val customerExists = customerRepository.containsCustomer(customer.id)
            if (!customerExists) {
                throw UseCaseException("Customer with the id '${customer.id}. already exists.")
            }

            try {
                customerRepository.updateCustomer(customer)
            } catch (e: RepositoryException) {
                val message = "Failed to update the customer with id '${customer.id}."
                logger.error(message, e)
                throw UseCaseException(message, e)
            }
        }
    }
}