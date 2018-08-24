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
    fun shouldInsertNewItem() {
        val item = createDummyItem(1)
        itemRepository.insertItem(item)

        assertThat(itemRepository.getAllItems().size).isOne()
        assertThat(itemRepository.getItemById(1)).isEqualTo(item)
    }

    @Test
    fun shouldFailToInsertDuplicateItems() {
        val item = createDummyItem(1)

        itemRepository.insertItem(item)
        assertThrows(RepositoryException::class.java) { itemRepository.insertItem(item) }
    }

    @Test
    fun shouldRemoveExistingItem() {
        assertThat(itemRepository.getAllItems().size).isZero()
        val item = createDummyItem(1)

        itemRepository.insertItem(item)

        itemRepository.removeItem(item)
        assertThat(itemRepository.getAllItems().size).isZero()
        assertThat(itemRepository.containsItem(item.id)).isFalse()
    }

    @Test
    fun shouldFailToRemoveNonExistingItem() {
        val item = createDummyItem(1)
        assertThrows(RepositoryException::class.java) { itemRepository.removeItem(item) }
    }

    @Test
    fun shouldUpdateAnExistingItem() {
        val item = createDummyItem(1)

        itemRepository.insertItem(item)
        assertThat(itemRepository.getItemById(1)).isEqualTo(item)

        val changedItem = Item(1, "name2", Dimension(1.0, 1.0, Unit.M2), "description2", 2, 2.0)
        itemRepository.updateItem(changedItem)

        assertThat(itemRepository.getItemById(1).name).isEqualTo("name2")
        assertThat(itemRepository.getItemById(1).description).isEqualTo("description2")
        assertThat(itemRepository.getItemById(1).amount).isEqualTo(2)
        assertThat(itemRepository.getItemById(1).pricePerUnit).isEqualTo(2.0)
    }

    @Test
    fun shouldFailToUpdateNonExistingItem() {
        val item = createDummyItem(1)
        assertThrows(RepositoryException::class.java) { itemRepository.updateItem(item) }
    }

    @Test
    fun shouldContainItem() {
        val item = createDummyItem(1)

        itemRepository.insertItem(item)
        assertThat(itemRepository.containsItem(1)).isTrue()
    }

    @Test
    fun shouldNotContainItem() {
        val item = createDummyItem(1)

        itemRepository.insertItem(item)
        assertThat(itemRepository.containsItem(2)).isFalse()
    }

    @Test
    fun shouldGetExistingItemById() {
        val item = createDummyItem(1)

        itemRepository.insertItem(item)
        assertThat(itemRepository.getItemById(1)).isEqualTo(item)
    }

    @Test
    fun shouldFailToGetNonExistingItemById() {
        val item = createDummyItem(1)

        itemRepository.insertItem(item)
        assertThrows(RepositoryException::class.java) { itemRepository.getItemById(2)}
    }

    @Test
    fun shouldGetAllItems() {
        val item1 = createDummyItem(1)
        val item2 = createDummyItem(2)

        itemRepository.insertItem(item1)
        itemRepository.insertItem(item2)

        val items = itemRepository.getAllItems()
        assertThat(items.size).isEqualTo(2)
        assertThat(items[0]).isEqualTo(item1)
        assertThat(items[1]).isEqualTo(item2)
    }

    @Test
    fun shouldGetNoItemsOnNoneInserted() {
        assertThat(itemRepository.getAllItems().size).isZero()
    }

    private fun createDummyItem(id: Int): Item = Item(id, 
            "name", 
            Dimension(1.0, 1.0, Unit.M2), 
            "description",
            1,
            1.0)
}