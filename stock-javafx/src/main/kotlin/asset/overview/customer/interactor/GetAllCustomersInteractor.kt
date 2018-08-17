package asset.overview.customer.interactor

import application.StockApplication
import application.executor.Executor
import repository.RepositoryException
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject
import javax.inject.Named

class GetAllCustomersInteractor(private val getAllCustomersOutput: GetAllCustomersOutput) {

    init {
        StockApplication.stockComponent.inject(this)
    }

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    @field:[Inject Named("worker")]
    internal lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    internal lateinit var mainExecutor: Executor

    fun getAllCustomers() {
        workerExecutor.submit(Runnable {
            try {
                val customers = customerRepository.getAllCustomers()
                mainExecutor.submit(Runnable { getAllCustomersOutput.onCustomersRetrieved(customers) })
            } catch (e: RepositoryException) {
                mainExecutor.submit(Runnable { getAllCustomersOutput.onRetrievalFailed("Failed to retrieve customers. Error: ${e.message}") })
            }
        })
    }
}