package asset.overview.item.interactor

import application.StockApplication
import application.executor.Executor
import repository.RepositoryException
import repository.item.ObservableItemRepository
import javax.inject.Inject
import javax.inject.Named

class GetAllItemsInteractor(private val getAllItemsOutput: GetAllItemsOutput) {

    init {
        StockApplication.stockComponent.inject(this)
    }

    @Inject
    internal lateinit var suppliersRepository: ObservableItemRepository

    @field:[Inject Named("worker")]
    internal lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    internal lateinit var mainExecutor: Executor

    fun getAllItems() {
        workerExecutor.submit(Runnable {
            try {
                val suppliers = suppliersRepository.getAllItems()
                mainExecutor.submit(Runnable { getAllItemsOutput.onItemsRetrieved(suppliers) })
            } catch (e: RepositoryException) {
                mainExecutor.submit(Runnable { getAllItemsOutput.onRetrievalFailed("Failed to retrieve customers. Error: ${e.message}") })
            }
        })
    }
}