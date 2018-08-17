package asset.add.supplier.interactor

import application.StockApplication
import application.executor.Executor
import repository.RepositoryException
import repository.supplier.ObservableSupplierRepository
import repository.supplier.Supplier
import javax.inject.Inject
import javax.inject.Named

class AddSupplierInteractor(private val addSupplierOutput: AddSupplierOutput) {

    init {
        StockApplication.stockComponent.inject(this)
    }

    @Inject
    lateinit var supplierRepository: ObservableSupplierRepository

    @field:[Inject Named("worker")]
    lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    lateinit var mainExecutor: Executor

    fun addNewSupplier(id: Int, name: String, accountNumber: String, address: String) {
        workerExecutor.submit(Runnable {
            try {
                val supplier = Supplier(id, name, accountNumber, address)
                supplierRepository.insertSupplier(supplier)
                mainExecutor.submit(Runnable { addSupplierOutput.onInsertionSuccessful() })
            } catch (e: RepositoryException) {
                mainExecutor.submit(Runnable { addSupplierOutput.onInsertionFailed("Failed to insert supplier. Error: ${e.message}") })
            } catch (e: RuntimeException) {
                mainExecutor.submit(Runnable { addSupplierOutput.onInsertionFailed("Failed to insert supplier. Error: ${e.message}") })
            }
        })
    }
}