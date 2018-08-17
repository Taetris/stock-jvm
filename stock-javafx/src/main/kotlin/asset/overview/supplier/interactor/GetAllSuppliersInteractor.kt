package asset.overview.supplier.interactor

import application.StockApplication
import application.executor.Executor
import repository.RepositoryException
import repository.supplier.ObservableSupplierRepository
import javax.inject.Inject
import javax.inject.Named

class GetAllSuppliersInteractor(private val getAllSuppliersOutput: GetAllSuppliersOutput) {

    init {
        StockApplication.stockComponent.inject(this)
    }

    @Inject
    internal lateinit var suppliersRepository: ObservableSupplierRepository

    @field:[Inject Named("worker")]
    internal lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    internal lateinit var mainExecutor: Executor

    fun getAllSuppliers() {
        workerExecutor.submit(Runnable {
            try {
                val suppliers = suppliersRepository.getAllSuppliers()
                mainExecutor.submit(Runnable { getAllSuppliersOutput.onSuppliersRetrieved(suppliers) })
            } catch (e: RepositoryException) {
                mainExecutor.submit(Runnable { getAllSuppliersOutput.onRetrievalFailed("Failed to retrieve customers. Error: ${e.message}") })
            }
        })
    }
}