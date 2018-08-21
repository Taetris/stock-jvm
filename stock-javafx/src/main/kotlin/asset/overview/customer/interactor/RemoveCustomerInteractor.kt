package asset.overview.customer.interactor

import application.StockApplication
import application.executor.Executor
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.customer.Customer
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject
import javax.inject.Named

class RemoveCustomerInteractor(private val removeCustomersOutput: RemoveCustomersOutput) {

    companion object {

        private val logger = LoggerFactory.getLogger(RemoveCustomerInteractor::class.java)
    }

    init {
        StockApplication.stockComponent.inject(this)
    }

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    @field:[Inject Named("worker")]
    internal lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    internal lateinit var mainExecutor: Executor

    fun removeCustomer(customer: Customer) {
        workerExecutor.submit(Runnable {
            logger.info("Removing customer '$customer'.")
            try {
                customerRepository.removeCustomer(customer)
            } catch (e: RepositoryException) {
                mainExecutor.submit(Runnable { removeCustomersOutput.onRemovalFailed(e.message) })
            }
        })
    }
}