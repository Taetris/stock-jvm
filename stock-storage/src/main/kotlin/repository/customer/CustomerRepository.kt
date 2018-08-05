package repository.customer

import repository.RepositoryException

/**
 * Represents the repository for storing of all customers.
 */
interface CustomerRepository {

    /**
     * Inserts a new customer into the repository. This operation will fail with an exception if there is a customer
     * stored, that is associated with the same id. To check if a customer already exists, use [containsCustomer] method.
     *
     * @param customer [Customer] the customer to insert.
     * @throws RepositoryException in case the same customer already exists, or there was another issue with the
     * insertion.
     */
    @Throws(RepositoryException::class)
    fun insertCustomer(customer: Customer)

    /**
     * Removes an existing customer from the repository. This operation will fail with an exception if the given
     * customer does not exist. To check if a customer exists, use [containsCustomer] method.
     *
     * @param customer [Customer]: customer to remove.
     * @throws RepositoryException in the customer does not exist, or there was another issue with the removal.
     */
    @Throws(RepositoryException::class)
    fun removeCustomer(customer: Customer)

    /**
     * Updates an existing customer with new values. Customer associated with the id of the new customer will be removed
     * and replaced with the new customer. If the given customer does not exist, the operation will fail with an
     * exception. To check if a customer exists, use [containsCustomer] method.
     *
     * @param customer [Customer]: updated customer that should replace the existing one.
     * @throws RepositoryException in case there was no customer associated, or there was another issue with the update.
     */
    @Throws(RepositoryException::class)
    fun updateCustomer(customer: Customer)

    /**
     * Checks if there is a customer stored, that is associated with the given id.
     *
     * @param id: id of the customer
     * @return [Boolean]: true if there's a customer associated with the given id, otherwise false.
     */
    @Throws(RepositoryException::class)
    fun containsCustomer(id: Int): Boolean

    /**
     * Gets the customer with the assigned id. If the given customer does not exist, the operation will fail with an
     * exception. To check if a customer exists, use [containsCustomer] method.
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