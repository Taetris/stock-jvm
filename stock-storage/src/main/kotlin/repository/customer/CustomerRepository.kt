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
     * @param customer [Customer]: customer to remove.
     * @throws RepositoryException in case deletion has failed.
     */
    @Throws(RepositoryException::class)
    fun removeCustomer(customer: Customer)

    /**
     * Updates an existing customer with new values. Customer associated with the id of the
     * new customer will be removed and replaced with the new customer.
     *
     * @param customer [Customer]: updated customer that should replace the existing one.
     * @throws RepositoryException in case update has failed.
     */
    @Throws(RepositoryException::class)
    fun updateCustomer(customer: Customer)

    /**
     * Checks if there is a customer stored, associated with the given id.
     *
     * @param id: id of the customer
     * @return [Boolean]: true if there's a customer associated with the given id, otherwise false.
     */
    @Throws(RepositoryException::class)
    fun containsCustomer(id: Int): Boolean

    /**
     * Gets the customer with assigned id.
     *
     * @param id: id of the customer to retrieve.
     * @return [Customer] the retrieved customer.
     * @throws RepositoryException in case there was no customer associated with the given id or there
     * was another issue with the retrieval.
     */
    @Throws(RepositoryException::class)
    fun getCustomerById(id: Int): Customer

    /**
     * Gets all customers from the repository.
     *
     * @return List<[Customer]> the list of retrieved customers.
     * @throws RepositoryException in case retrieval has failed.
     */
    @Throws(RepositoryException::class)
    fun getAllCustomers(): List<Customer>
}