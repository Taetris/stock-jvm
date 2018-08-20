package repository.item

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.RepositoryException

import repository.item.mock.ObservableItemRepositoryMock

internal class ItemRepositoryTest {

    private lateinit var itemRepository: ObservableItemRepository

    @BeforeEach
    fun setUp() {
        itemRepository = ObservableItemRepositoryMock()
    }

    @Test
    fun shouldInsertNewSupplier() {
        val supplier = createDummySupplier(1)
        itemRepository.insertItem(supplier)

        assertThat(itemRepository.getAllItems().size).isOne()
        assertThat(itemRepository.getItemById(1)).isEqualTo(supplier)
    }

    @Test
    fun shouldFailToInsertDuplicateSuppliers() {
        val supplier = createDummySupplier(1)

        itemRepository.insertItem(supplier)
        assertThrows(RepositoryException::class.java) { itemRepository.insertItem(supplier) }
    }

    @Test
    fun shouldRemoveExistingSupplier() {
        assertThat(itemRepository.getAllItems().size).isZero()
        val supplier = createDummySupplier(1)

        itemRepository.insertItem(supplier)

        itemRepository.removeItem(supplier)
        assertThat(itemRepository.getAllItems().size).isZero()
        assertThat(itemRepository.containsItem(supplier.id)).isFalse()
    }

    @Test
    fun shouldFailToRemoveNonExistingSupplier() {
        val supplier = createDummySupplier(1)
        assertThrows(RepositoryException::class.java) { itemRepository.removeItem(supplier) }
    }

    @Test
    fun shouldUpdateAnExistingSupplier() {
        val supplier = createDummySupplier(1)

        itemRepository.insertItem(supplier)
        assertThat(itemRepository.getItemById(1)).isEqualTo(supplier)

        val changedSupplier = Item(1, "name2", "2", "address2")
        itemRepository.updateItem(changedSupplier)

        assertThat(itemRepository.getItemById(1).name).isEqualTo("name2")
        assertThat(itemRepository.getItemById(1).accountNumber).isEqualTo("2")
        assertThat(itemRepository.getItemById(1).address).isEqualTo("address2")
    }

    @Test
    fun shouldFailToUpdateNonExistingSupplier() {
        val supplier = Item(1, "name2", "2", "address2")
        assertThrows(RepositoryException::class.java) { itemRepository.updateItem(supplier) }
    }

    @Test
    fun shouldContainSupplier() {
        val supplier = createDummySupplier(1)

        itemRepository.insertItem(supplier)
        assertThat(itemRepository.containsItem(1)).isTrue()
    }

    @Test
    fun shouldNotContainSupplier() {
        val supplier = createDummySupplier(1)

        itemRepository.insertItem(supplier)
        assertThat(itemRepository.containsItem(2)).isFalse()
    }

    @Test
    fun shouldGetExistingSupplierById() {
        val supplier = createDummySupplier(1)

        itemRepository.insertItem(supplier)
        assertThat(itemRepository.getItemById(1)).isEqualTo(supplier)
    }

    @Test
    fun shouldFailToGetNonExistingSupplierById() {
        val supplier = createDummySupplier(1)

        itemRepository.insertItem(supplier)
        assertThrows(RepositoryException::class.java) { itemRepository.getItemById(2)}
    }

    @Test
    fun shouldGetAllSuppliers() {
        val supplier1 = createDummySupplier(1)
        val supplier2 = createDummySupplier(2)

        itemRepository.insertItem(supplier1)
        itemRepository.insertItem(supplier2)

        val suppliers = itemRepository.getAllItems()
        assertThat(suppliers.size).isEqualTo(2)
        assertThat(suppliers[0]).isEqualTo(supplier1)
        assertThat(suppliers[1]).isEqualTo(supplier2)
    }

    @Test
    fun shouldGetNoSuppliersOnNoneInserted() {
        assertThat(itemRepository.getAllItems().size).isZero()
    }

    private fun createDummySupplier(id: Int): Item = Item(id, "name", "accountNumber", "address")
}