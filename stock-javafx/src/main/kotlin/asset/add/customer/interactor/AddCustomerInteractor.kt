package asset.add.customer.interactor

import application.StockApplication
import application.executor.Executor
import repository.RepositoryException
import repository.customer.Customer
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject
import javax.inject.Named

class AddCustomerInteractor(private val addCustomerOutput: AddCustomerOutput) {

    init {
        StockApplication.stockComponent.inject(this)
    }

    @Inject
    lateinit var customerRepository: ObservableCustomerRepository

    @field:[Inject Named("worker")]
    lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    lateinit var mainExecutor: Executor

    fun addNewCustomer(id: Int, name: String, accountNumber: String, address: String) {
        workerExecutor.submit(Runnable {
            try {
                val customer = Customer(id, name, accountNumber, address)
                customerRepository.insertCustomer(customer)
                mainExecutor.submit(Runnable { addCustomerOutput.onInsertionSuccessful() })
            } catch (e: RepositoryException) {
                mainExecutor.submit(Runnable { addCustomerOutput.onInsertionFailed("Failed to insert customer. Error: ${e.message}") })
            } catch (e: RuntimeException) {
                mainExecutor.submit(Runnable { addCustomerOutput.onInsertionFailed("Failed to insert customer. Error: ${e.message}") })
            }
        })
    }
}