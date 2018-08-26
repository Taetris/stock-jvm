package asset.item.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.item.Dimension
import repository.item.Item
import repository.item.ObservableItemRepository
import repository.item.Unit
import javax.inject.Inject

class AddItemUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(AddItemUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    @Throws(UseCaseException::class)
    suspend fun addNewItem(id: Int, name: String,
                           dimension: String,
                           description: String,
                           amount: Int,
                           unit: String,
                           pricePerUnit: Double) {
        withContext(CommonPool) {
            val item = createItem(id, name, dimension, description, amount, unit, pricePerUnit)
            logger.info("Adding new item $item")

            try {
                itemRepository.insertItem(item)
            } catch (e: RepositoryException) {
                val message = "Failed to add a new item."
                logger.error(message, e)
                throw UseCaseException(message, e)
            }
        }
    }

    @Throws(UseCaseException::class)
    private fun createItem(id: Int, name: String,
                           dimension: String,
                           description: String,
                           amount: Int,
                           unit: String,
                           pricePerUnit: Double) : Item {
        try {
            val convertedUnit = Unit.fromString(unit)
            val convertedDimension = Dimension.fromString(dimension, convertedUnit)

            return Item(id, name, convertedDimension, description, amount, pricePerUnit)
        } catch (e: IllegalArgumentException) {
            val message = "Failed to create an item."
            logger.error(message, e)
            throw UseCaseException(message)
        }
    }
}