package repository.customer

import repository.RepositoryException

/**
 * Represents the repository for storing of all customers.
 */
interface CustomerRepository {

    /**
     * Inserts a new customer into the repository.
     *
     * @param customer [Customer] the customer to insert.
     * @throws RepositoryException in case insertion has failed.
     */
    @Throws(RepositoryException::class)
    fun insertCustomer(customer: Customer)

    /**
     * Removes an existing customer from the repository.
     *
     * @param id: id of the customer to remove.
     * @throws RepositoryException in case deletion has failed.
     */
    @Throws(RepositoryException::class)
    fun removeCustomer(id: Int)

    /**
     * Updates an existing customer with new values.
     *
     * @param customer [Customer] updated customer that should replace the existing one.
     * @throws RepositoryException in case update has failed.
     */
    @Throws(RepositoryException::class)
    fun updateCustomer(customer: Customer)

    /**
     * Gets the customer with assigned id.
     *
     * @param id: id of the customer to retrieve.
     * @return [Customer] the retrieved customer.
     * @throws RepositoryException in case retrieval has failed.
     */
    @Throws(RepositoryException::class)
    fun getCustomer(id: Int): Customer

    /**
     * Gets all customers from the repository.
     *
     * @return List<[Customer]> the list of retrieved customers.
     * @throws RepositoryException in case retrieval has failed.
     */
    @Throws(RepositoryException::class)
    fun getAllCustomers(): List<Customer>
}