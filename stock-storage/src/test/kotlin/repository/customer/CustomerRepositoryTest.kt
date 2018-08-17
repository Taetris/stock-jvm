package repository.customer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.RepositoryException
import repository.customer.mock.ObservableCustomerRepositoryMock

internal class CustomerRepositoryTest {

    private lateinit var customerRepository: ObservableCustomerRepository

    @BeforeEach
    fun setUp() {
        customerRepository = ObservableCustomerRepositoryMock()
    }

    @Test
    fun shouldInsertNewCustomer() {
        val customer = createDummyCustomer(1)
        customerRepository.insertCustomer(customer)

        assertThat(customerRepository.getAllCustomers().size).isOne()
        assertThat(customerRepository.getCustomerById(1)).isEqualTo(customer)
    }

    @Test
    fun shouldFailToInsertDuplicateCustomers() {
        val customer = createDummyCustomer(1)

        customerRepository.insertCustomer(customer)
        assertThrows(RepositoryException::class.java) { customerRepository.insertCustomer(customer) }
    }

    @Test
    fun shouldRemoveExistingCustomer() {
        assertThat(customerRepository.getAllCustomers().size).isZero()
        val customer = createDummyCustomer(1)

        customerRepository.insertCustomer(customer)

        customerRepository.removeCustomer(customer)
        assertThat(customerRepository.getAllCustomers().size).isZero()
        assertThat(customerRepository.containsCustomer(customer.id)).isFalse()
    }

    @Test
    fun shouldFailToRemoveNonExistingCustomer() {
        val customer = createDummyCustomer(1)
        assertThrows(RepositoryException::class.java) { customerRepository.removeCustomer(customer) }
    }

    @Test
    fun shouldUpdateAnExistingCustomer() {
        val customer = createDummyCustomer(1)

        customerRepository.insertCustomer(customer)
        assertThat(customerRepository.getCustomerById(1)).isEqualTo(customer)

        val changedCustomer = Customer(1, "name2", "2", "address2")
        customerRepository.updateCustomer(changedCustomer)

        assertThat(customerRepository.getCustomerById(1).name).isEqualTo("name2")
        assertThat(customerRepository.getCustomerById(1).accountNumber).isEqualTo("2")
        assertThat(customerRepository.getCustomerById(1).address).isEqualTo("address2")
    }

    @Test
    fun shouldFailToUpdateNonExistingCustomer() {
        val customer = Customer(1, "name2", "2", "address2")
        assertThrows(RepositoryException::class.java) { customerRepository.updateCustomer(customer) }
    }

    @Test
    fun shouldContainCustomer() {
        val customer = createDummyCustomer(1)

        customerRepository.insertCustomer(customer)
        assertThat(customerRepository.containsCustomer(1)).isTrue()
    }

    @Test
    fun shouldNotContainCustomer() {
        val customer = createDummyCustomer(1)

        customerRepository.insertCustomer(customer)
        assertThat(customerRepository.containsCustomer(2)).isFalse()
    }

    @Test
    fun shouldGetExistingCustomerById() {
        val customer = createDummyCustomer(1)

        customerRepository.insertCustomer(customer)
        assertThat(customerRepository.getCustomerById(1)).isEqualTo(customer)
    }

    @Test
    fun shouldFailToGetNonExistingCustomerById() {
        val customer = createDummyCustomer(1)

        customerRepository.insertCustomer(customer)
        assertThrows(RepositoryException::class.java) { customerRepository.getCustomerById(2) }
    }

    @Test
    fun shouldGetAllCustomers() {
        val customer1 = createDummyCustomer(1)
        val customer2 = createDummyCustomer(2)

        customerRepository.insertCustomer(customer1)
        customerRepository.insertCustomer(customer2)

        val customers = customerRepository.getAllCustomers()
        assertThat(customers.size).isEqualTo(2)
        assertThat(customers[0]).isEqualTo(customer1)
        assertThat(customers[1]).isEqualTo(customer2)
    }

    @Test
    fun shouldGetNoCustomersOnNoneInserted() {
        assertThat(customerRepository.getAllCustomers().size).isZero()
    }

    private fun createDummyCustomer(id: Int): Customer = Customer(id, "name", "accountNumber", "address")
}