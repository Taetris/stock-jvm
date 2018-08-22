package asset.customer.subject

import application.executor.UI
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.RepositoryObserver
import repository.customer.ObservableCustomerRepository

class CustomerSubject(customerRepository: ObservableCustomerRepository) : RepositoryObserver {

    private val logger = LoggerFactory.getLogger(CustomerSubject::class.java)
    private val observers = ArrayList<CustomerObserver>()

    init {
        customerRepository.register(this)
    }

    fun register(observer: CustomerObserver) {
        logger.info("Registering Observer '$observer")
        observers.add(observer)
    }

    override fun onRepositoryChanged() {
        logger.info("Customer repository has changed")
        launch(UI) { observers.forEach { it.onCustomersChanged() } }
    }
}