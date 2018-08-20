package asset.subject.item

import application.StockApplication
import application.executor.Executor
import org.slf4j.LoggerFactory
import repository.RepositoryObserver
import repository.item.ObservableItemRepository
import javax.inject.Inject
import javax.inject.Named

class ItemSubject : RepositoryObserver {

    private val logger = LoggerFactory.getLogger(ItemSubject::class.java)
    private val observers = ArrayList<ItemObserver>()

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    @field:[Inject Named("worker")]
    internal lateinit var workerExecutor: Executor

    @field:[Inject Named("main")]
    internal lateinit var mainExecutor: Executor

    init {
        StockApplication.stockComponent.inject(this)
        itemRepository.register(this)
    }

    fun register(observer: ItemObserver) {
        logger.info("Registering Observer '$observer")
        observers.add(observer)
    }

    override fun onRepositoryChanged() {
        logger.info("Item repository has changed")
        mainExecutor.submit(Runnable { observers.forEach { it.onItemsChanged() } })
    }
}