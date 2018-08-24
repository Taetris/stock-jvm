package repository.item.mock

import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.RepositoryObserver
import repository.item.Dimension
import repository.item.Item
import repository.item.ObservableItemRepository
import repository.item.Unit

/**
 * Mock implementation of the [ObservableItemRepository] that uses an in-memory storage.
 */
class ObservableItemRepositoryMock : ObservableItemRepository {

    private val logger = LoggerFactory.getLogger(ObservableItemRepositoryMock::class.java)
    private val items: MutableList<Item> = ArrayList()
    private  val observers: MutableList<RepositoryObserver> = ArrayList()

    init {
        items.add(Item(1, "name1", Dimension(1.0, 1.0, Unit.M2), "description1", 5, 23.0))
        items.add(Item(2, "name2", Dimension(2.0, 1.6, Unit.M2), "description2", 4, 64.0))
        items.add(Item(3, "name3", Dimension(4.2, 3.0, Unit.M2), "description3", 3, 4.0))
        items.add(Item(4, "name4", Dimension(8.0, 1.0, Unit.M), "description4", 2, 6.0))
        items.add(Item(5, "name5", Dimension(2.0, 4.0, Unit.M2), "description5", 7, 12.0))
        items.add(Item(6, "name6", Dimension(5.0, 1.0, Unit.M), "description6", 2, 20.0))
    }

    override fun insertItem(item: Item) {
        if (!containsItem(item.id)) {
            items.add(item)
            logger.info("Inserted item: '{}'", item)
            notifyObservers()
        } else {
            throw RepositoryException("Failed to insert item '$item'.")
        }
    }

    override fun removeItem(item: Item) {
        if (containsItem(item.id)) {
            items.remove(item)
            logger.info("Removed item '{}'", item)
            notifyObservers()
        } else {
            throw RepositoryException("Failed to remove item '$item'.")
        }
    }

    override fun updateItem(item: Item) {
        items.forEach { other ->
            if (other.id == item.id) {
                items.remove(other)
                items.add(item)
                logger.info("Updated item: '{}'", item)
                notifyObservers()
                return
            }
        }

        throw RepositoryException("Failed to update item with the id '${item.id}'.")
    }

    override fun containsItem(id: Int): Boolean {
        var result = false
        items.forEach { item ->
            if (item.id == id) {
                result = true
            }
        }

        logger.info("Contains item with id '$id': $result")
        return result    }

    override fun getItemById(id: Int): Item {
        items.forEach { item ->
            if (item.id == id) {
                return item
            }
        }

        throw RepositoryException("No item found associated with the id '$id'.")    }

    override fun getAllItems(): List<Item> {
        return items
    }

    override fun register(repositoryObserver: RepositoryObserver) {
        if (!observers.contains(repositoryObserver)) {
            observers.add(repositoryObserver)
            logger.info("Added observer '$repositoryObserver'")
        }
    }

    override fun notifyObservers() {
        observers.forEach { observer ->
            logger.info("Notifying observer '$observer'")
            observer.onRepositoryChanged()
        }
    }
}