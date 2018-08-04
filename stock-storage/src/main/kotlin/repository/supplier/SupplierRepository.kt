package repository.supplier

import repository.RepositoryException

/**
 * Represents the repository for storing of all suppliers.
 */
interface SupplierRepository {

    /**
     * Inserts a new supplier into the repository.
     *
     * @param supplier [Supplier] the supplier to insert.
     * @throws RepositoryException in case insertion has failed.
     */
    @Throws(RepositoryException::class)
    fun insertSupplier(supplier: Supplier)

    /**
     * Removes an existing supplier from the repository.
     *
     * @param id: id of the supplier to remove.
     * @throws RepositoryException in case deletion has failed.
     */
    @Throws(RepositoryException::class)
    fun removeSupplier(id: Int)

    /**
     * Updates an existing supplier with new values.
     *
     * @param supplier [Supplier] updated supplier that should replace the existing one.
     * @throws RepositoryException in case update has failed.
     */
    @Throws(RepositoryException::class)
    fun updateSupplier(supplier: Supplier)

    /**
     * Gets the supplier with assigned id.
     *
     * @param id: id of the supplier to retrieve.
     * @return [Supplier] the retrieved supplier.
     * @throws RepositoryException in case retrieval has failed.
     */
    @Throws(RepositoryException::class)
    fun getSupplier(id: Int): Supplier

    /**
     * Gets all suppliers from the repository.
     *
     * @return List<[Supplier]> the list of retrieved suppliers.
     * @throws RepositoryException in case retrieval has failed.
     */
    @Throws(RepositoryException::class)
    fun getAllSuppliers(): List<Supplier>
}