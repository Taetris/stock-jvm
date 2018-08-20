package repository.item

import repository.RepositoryException
import repository.RepositoryObservable

/**
 * Represents the repository for storing of all items. In case of a change it will notify all of its observers.
 */
interface ObservableItemRepository : RepositoryObservable {

    /**
     * Inserts a new item into the repository. This operation will fail with an exception if there is an item
     * stored that is associated with the same id. To check if an item already exists, use [containsItem] method.
     * In case of a successful insertion, all observers are notified.
     *
     * @param item [Item] the item to insert.
     * @throws RepositoryException in case the same item already exists, or there was another issue with the
     * insertion.
     */
    @Throws(RepositoryException::class)
    fun insertItem(item: Item)

    /**
     * Removes an existing item from the repository. This operation will fail with an exception if the given
     * item does not exist. To check if an item exists, use [containsItem] method. In case of a successful
     * removal, all observers are notified.
     *
     * @param item [Item]: item to remove.
     * @throws RepositoryException in case the item does not exist, or there was another issue with the removal.
     */
    @Throws(RepositoryException::class)
    fun removeItem(item: Item)

    /**
     * Updates an existing item with new values. Item associated with the id of the new item will be removed
     * and replaced with the new item. If the given item does not exist, the operation will fail with an
     * exception. To check if an item exists, use [containsItem] method. In case of a successful update, all
     * observers are notified.
     *
     * @param item [Item]: updated item that should replace the existing one.
     * @throws RepositoryException in case there was no item associated, or there was another issue with the update.
     */
    @Throws(RepositoryException::class)
    fun updateItem(item: Item)

    /**
     * Checks if there is an item stored, that is associated with the given id.
     *
     * @param id: id of the item.
     * @return [Boolean]: true if there's an item associated with the given id, otherwise false.
     */
    @Throws(RepositoryException::class)
    fun containsItem(id: Int): Boolean

    /**
     * Gets the item with the assigned id. If the given item does not exist, the operation will fail with an
     * exception. To check if an item exists, use [containsItem] method.
     *
     * @param id: id of the item to retrieve.
     * @return [Item] the retrieved item.
     * @throws RepositoryException in case there was no item associated with the given id or there
     * was another issue with the retrieval.
     */
    @Throws(RepositoryException::class)
    fun getItemById(id: Int): Item

    /**
     * Gets all items from the repository.
     *
     * @return List<[Item]> the list of retrieved items.
     * @throws RepositoryException in case retrieval has failed.
     */
    @Throws(RepositoryException::class)
    fun getAllItems(): List<Item>
}