package asset.subject.customer

import application.StockApplication
import application.executor.Executor
import org.slf4j.LoggerFactory
import repository.RepositoryObserver
import repository.customer.ObservableCustomerRepository
import javax.inject.Inject
import javax.inject.Named

class CustomerSubject: RepositoryObserver {

    private val logger = LoggerFactory.getLogger(CustomerSubject::class.java)
    private val observers = ArrayList<CustomerObserver>()

    @Inject
    internal lateinit var customerRepository: ObservableCustomerRepository

    @field:[Inject Named("worker")]
    internal lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    internal lateinit var mainExecutor: Executor

    init {
        StockApplication.stockComponent.inject(this)
        customerRepository.register(this)
    }

    fun register(observer: CustomerObserver) {
        logger.info("Registering Observer '$observer")
        observers.add(observer)
    }

    override fun onRepositoryChanged() {
        logger.info("Customer repository has changed")
        mainExecutor.submit(Runnable { observers.forEach { it.onCustomersChanged() } })
    }
}