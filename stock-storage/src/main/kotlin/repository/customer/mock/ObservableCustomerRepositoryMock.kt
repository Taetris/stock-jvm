package repository.customer.mock

import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.RepositoryObserver
import repository.customer.Customer
import repository.customer.ObservableCustomerRepository

/**
 * Mock implementation of the [ObservableCustomerRepository] that uses an in-memory storage.
 */
class ObservableCustomerRepositoryMock : ObservableCustomerRepository {

    private val logger = LoggerFactory.getLogger(ObservableCustomerRepositoryMock::class.java)
    private val customers: MutableList<Customer> = ArrayList()
    private val observers: MutableList<RepositoryObserver> = ArrayList()

    init {
        customers.add(Customer(1, "Name1", "idNumber1", "pdvNumber1", "address1"))
        customers.add(Customer(2, "Name2", "idNumber2", "pdvNumber2", "address2"))
        customers.add(Customer(3, "Name3", "idNumber3", "pdvNumber3", "address3"))
        customers.add(Customer(4, "Name4", "idNumber4", "pdvNumber4", "address4"))
        customers.add(Customer(5, "Name5", "idNumber5", "pdvNumber5", "address5"))
        customers.add(Customer(6, "Name6", "idNumber6", "pdvNumber6", "address6"))
    }

    override fun insertCustomer(customer: Customer) {
        if (!containsCustomer(customer.id)) {
            customers.add(customer)
            logger.info("Inserted customer: '{}'", customer)
            notifyObservers()
        } else {
            throw RepositoryException("Failed to insert customer '$customer'.")
        }
    }

    override fun removeCustomer(customer: Customer) {
        if (containsCustomer(customer.id)) {
            customers.remove(customer)
            logger.info("Removed customer '{}'", customer)
            notifyObservers()
        } else {
            throw RepositoryException("Failed to remove customer '$customer'.")
        }
    }

    override fun updateCustomer(customer: Customer) {
        customers.forEach { other ->
            if (other.id == customer.id) {
                customers.remove(other)
                customers.add(customer)
                logger.info("Updated customer: '{}'", customer)
                notifyObservers()
                return
            }
        }

        throw RepositoryException("Failed to update customer with the id '${customer.id}'.")
    }

    override fun containsCustomer(id: Int): Boolean {
        var result = false
        customers.forEach { customer ->
            if (customer.id == id) {
                result = true
            }
        }

        logger.info("Contains customer with id '$id': $result")
        return result
    }

    override fun getCustomerById(id: Int): Customer {
        customers.forEach { customer ->
            if (customer.id == id) {
                return customer
            }
        }

        throw RepositoryException("No customer found associated with the id '$id'.")
    }

    override fun getAllCustomers(): List<Customer> {
        return customers
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