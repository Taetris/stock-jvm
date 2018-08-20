package asset.add.item.interactor

import application.StockApplication
import application.executor.Executor
import repository.RepositoryException
import repository.item.Item
import repository.item.ObservableItemRepository
import javax.inject.Inject
import javax.inject.Named

class AddItemInteractor(private val addItemOutput: AddItemOutput) {

    init {
        StockApplication.stockComponent.inject(this)
    }

    @Inject
    lateinit var itemRepository: ObservableItemRepository

    @field:[Inject Named("worker")]
    lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    lateinit var mainExecutor: Executor

    fun addNewItem(id: Int, name: String, accountNumber: String, address: String) {
        workerExecutor.submit(Runnable {
            try {
                val item = Item(id, name, accountNumber, address)
                itemRepository.insertItem(item)
                mainExecutor.submit(Runnable { addItemOutput.onInsertionSuccessful() })
            } catch (e: RepositoryException) {
                mainExecutor.submit(Runnable { addItemOutput.onInsertionFailed("Failed to insert item. Error: ${e.message}") })
            } catch (e: RuntimeException) {
                mainExecutor.submit(Runnable { addItemOutput.onInsertionFailed("Failed to insert item. Error: ${e.message}") })
            }
        })
    }
}