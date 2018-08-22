package asset.item.subject

import application.executor.UI
import kotlinx.coroutines.experimental.launch
import org.slf4j.LoggerFactory
import repository.RepositoryObserver
import repository.item.ObservableItemRepository

class ItemSubject(itemRepository: ObservableItemRepository) : RepositoryObserver {

    private val logger = LoggerFactory.getLogger(ItemSubject::class.java)
    private val observers = ArrayList<ItemObserver>()

    init {
        itemRepository.register(this)
    }

    fun register(observer: ItemObserver) {
        logger.info("Registering Observer '$observer")
        observers.add(observer)
    }

    override fun onRepositoryChanged() {
        logger.info("Item repository has changed")
        launch(UI) { observers.forEach { it.onItemsChanged() } }
    }
}