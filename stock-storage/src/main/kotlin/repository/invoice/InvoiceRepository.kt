package repository.invoice

import repository.RepositoryException

/**
 * Represents the repository for storing of all invoices.
 */
interface InvoiceRepository {

    /**
     * Gets the next valid invoice id for the wholesale group.
     *
     * @throws RepositoryException in case there was a technical issue when retrieving the id.
     */
    @Throws(RepositoryException::class)
    fun getNextInvoiceIdForWholesale(): Int

    /**
     * Gets the next valid invoice id for the retail group.
     *
     * @throws RepositoryException in case there was a technical issue when retrieving the id.
     */
    @Throws(RepositoryException::class)
    fun getNextInvoiceIdForRetail(): Int
}