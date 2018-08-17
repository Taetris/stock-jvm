package asset.subject.supplier

import application.StockApplication
import application.executor.Executor
import org.slf4j.LoggerFactory
import repository.RepositoryObserver
import repository.supplier.ObservableSupplierRepository
import javax.inject.Inject
import javax.inject.Named

class SupplierSubject : RepositoryObserver {

    private val logger = LoggerFactory.getLogger(SupplierSubject::class.java)
    private val observers = ArrayList<SupplierObserver>()

    @Inject
    internal lateinit var supplierRepository: ObservableSupplierRepository

    @field:[Inject Named("worker")]
    internal lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    internal lateinit var mainExecutor: Executor

    init {
        StockApplication.stockComponent.inject(this)
        supplierRepository.register(this)
    }

    fun register(observer: SupplierObserver) {
        logger.info("Registering Observer '$observer")
        observers.add(observer)
    }

    override fun onRepositoryChanged() {
        logger.info("Supplier repository has changed")
        mainExecutor.submit(Runnable { observers.forEach { it.onSuppliersChanged() } })
    }
}