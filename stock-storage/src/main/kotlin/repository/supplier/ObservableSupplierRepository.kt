package repository.supplier

import repository.RepositoryException
import repository.RepositoryObservable

/**
 * Represents the repository for storing of all suppliers. In case of a change it will notify all of its observers.
 */
interface ObservableSupplierRepository : RepositoryObservable {

    /**
     * Inserts a new supplier into the repository. This operation will fail with an exception if there is a supplier
     * stored, that is associated with the same id. To check if a supplier already exists, use [containsSupplier] method.
     * In case of a successful insertion, all observers are notified.
     *
     * @param supplier [Supplier] the supplier to insert.
     * @throws RepositoryException in case the same supplier already exists, or there was another issue with the
     * insertion.
     */
    @Throws(RepositoryException::class)
    fun insertSupplier(supplier: Supplier)

    /**
     * Removes an existing supplier from the repository. This operation will fail with an exception if the given
     * supplier does not exist. To check if a supplier exists, use [containsSupplier] method. In case of a successful
     * removal, all observers are notified.
     *
     * @param supplier [Supplier]: supplier to remove.
     * @throws RepositoryException in the supplier does not exist, or there was another issue with the removal.
     */
    @Throws(RepositoryException::class)
    fun removeSupplier(supplier: Supplier)

    /**
     * Updates an existing supplier with new values. Supplier associated with the id of the new supplier will be removed
     * and replaced with the new supplier. If the given supplier does not exist, the operation will fail with an
     * exception. To check if a supplier exists, use [containsSupplier] method. In case of a successful update, all
     * observers are notified.
     *
     * @param supplier [Supplier]: updated supplier that should replace the existing one.
     * @throws RepositoryException in case there was no supplier associated, or there was another issue with the update.
     */
    @Throws(RepositoryException::class)
    fun updateSupplier(supplier: Supplier)

    /**
     * Checks if there is a supplier stored, that is associated with the given id.
     *
     * @param id: id of the supplier.
     * @return [Boolean]: true if there's a supplier associated with the given id, otherwise false.
     */
    @Throws(RepositoryException::class)
    fun containsSupplier(id: Int): Boolean

    /**
     * Gets the supplier with the assigned id. If the given supplier does not exist, the operation will fail with an
     * exception. To check if a supplier exists, use [containsSupplier] method.
     *
     * @param id: id of the supplier to retrieve.
     * @return [Supplier] the retrieved supplier.
     * @throws RepositoryException in case there was no supplier associated with the given id or there
     * was another issue with the retrieval.
     */
    @Throws(RepositoryException::class)
    fun getSupplierById(id: Int): Supplier

    /**
     * Gets all suppliers from the repository.
     *
     * @return List<[Supplier]> the list of retrieved suppliers.
     * @throws RepositoryException in case retrieval has failed.
     */
    @Throws(RepositoryException::class)
    fun getAllSuppliers(): List<Supplier>
}