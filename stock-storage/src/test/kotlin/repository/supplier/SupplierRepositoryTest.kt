package repository.supplier

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.RepositoryException

import repository.supplier.mock.SupplierRepositoryMock

internal class SupplierRepositoryTest {

    private lateinit var supplierRepository: SupplierRepository

    @BeforeEach
    fun setUp() {
        supplierRepository = SupplierRepositoryMock()
    }

    @Test
    fun shouldInsertNewSupplier() {
        val supplier = createDummySupplier(1)
        supplierRepository.insertSupplier(supplier)

        assertThat(supplierRepository.getAllSuppliers().size).isOne()
        assertThat(supplierRepository.getSupplierById(1)).isEqualTo(supplier)
    }

    @Test
    fun shouldFailToInsertDuplicateSuppliers() {
        val supplier = createDummySupplier(1)

        supplierRepository.insertSupplier(supplier)
        assertThrows(RepositoryException::class.java) { supplierRepository.insertSupplier(supplier) }
    }

    @Test
    fun shouldRemoveExistingSupplier() {
        assertThat(supplierRepository.getAllSuppliers().size).isZero()
        val supplier = createDummySupplier(1)

        supplierRepository.insertSupplier(supplier)

        supplierRepository.removeSupplier(supplier)
        assertThat(supplierRepository.getAllSuppliers().size).isZero()
        assertThat(supplierRepository.containsSupplier(supplier.id)).isFalse()
    }

    @Test
    fun shouldFailToRemoveNonExistingSupplier() {
        val supplier = createDummySupplier(1)
        assertThrows(RepositoryException::class.java) { supplierRepository.removeSupplier(supplier) }
    }

    @Test
    fun shouldUpdateAnExistingSupplier() {
        val supplier = createDummySupplier(1)

        supplierRepository.insertSupplier(supplier)
        assertThat(supplierRepository.getSupplierById(1)).isEqualTo(supplier)

        val changedSupplier = Supplier(1, "name2", "2", "address2")
        supplierRepository.updateSupplier(changedSupplier)

        assertThat(supplierRepository.getSupplierById(1).name).isEqualTo("name2")
        assertThat(supplierRepository.getSupplierById(1).accountNumber).isEqualTo("2")
        assertThat(supplierRepository.getSupplierById(1).address).isEqualTo("address2")
    }

    @Test
    fun shouldFailToUpdateNonExistingSupplier() {
        val supplier = Supplier(1, "name2", "2", "address2")
        assertThrows(RepositoryException::class.java) { supplierRepository.updateSupplier(supplier) }
    }

    @Test
    fun shouldContainSupplier() {
        val supplier = createDummySupplier(1)

        supplierRepository.insertSupplier(supplier)
        assertThat(supplierRepository.containsSupplier(1)).isTrue()
    }

    @Test
    fun shouldNotContainSupplier() {
        val supplier = createDummySupplier(1)

        supplierRepository.insertSupplier(supplier)
        assertThat(supplierRepository.containsSupplier(2)).isFalse()
    }

    @Test
    fun shouldGetExistingSupplierById() {
        val supplier = createDummySupplier(1)

        supplierRepository.insertSupplier(supplier)
        assertThat(supplierRepository.getSupplierById(1)).isEqualTo(supplier)
    }

    @Test
    fun shouldFailToGetNonExistingSupplierById() {
        val supplier = createDummySupplier(1)

        supplierRepository.insertSupplier(supplier)
        assertThrows(RepositoryException::class.java) { supplierRepository.getSupplierById(2)}
    }

    @Test
    fun shouldGetAllSuppliers() {
        val supplier1 = createDummySupplier(1)
        val supplier2 = createDummySupplier(2)

        supplierRepository.insertSupplier(supplier1)
        supplierRepository.insertSupplier(supplier2)

        val suppliers = supplierRepository.getAllSuppliers()
        assertThat(suppliers.size).isEqualTo(2)
        assertThat(suppliers[0]).isEqualTo(supplier1)
        assertThat(suppliers[1]).isEqualTo(supplier2)
    }

    @Test
    fun shouldGetNoSuppliersOnNoneInserted() {
        assertThat(supplierRepository.getAllSuppliers().size).isZero()
    }

    private fun createDummySupplier(id: Int): Supplier = Supplier(id, "name", "accountNumber", "address")
}