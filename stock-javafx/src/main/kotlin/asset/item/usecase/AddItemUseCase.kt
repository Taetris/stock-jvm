package asset.item.usecase

import application.usecase.ErrorCode
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

/**
 * Use case for insertion of a new item.
 */
class AddItemUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(AddItemUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    /**
     * Inserts a new item into the repository. If the item already exists, or the insertion failed,
     * a [UseCaseException] will be thrown.
     *
     * Expected error codes:
     * [ErrorCode.ALREADY_EXISTS] - in case the operation fails due to an already existing item.
     * [ErrorCode.OPERATION_FAILED] - in case the insertion failed.
     */
    @Throws(UseCaseException::class)
    suspend fun addNewItem(id: Int, name: String,
                           dimension: String,
                           description: String,
                           amount: Int,
                           unit: String,
                           pricePerUnit: Double) {
        withContext(CommonPool) {
            logger.info("Adding a new item with id '$id'")

            if (itemRepository.containsItem(id)) {
                logger.error("Item with id '$id' already exists.")
                throw UseCaseException(ErrorCode.ALREADY_EXISTS)
            }


            try {
                val item = createItem(id, name, dimension, description, amount, unit, pricePerUnit)
                logger.info("Adding new item $item")
                itemRepository.insertItem(item)
            } catch (e: RepositoryException) {
                val message = "Failed to add a new item. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            } catch (e: IllegalArgumentException) {
                val message = "Invalid input parameters. Error: '${e.message}."
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.INVALID_INPUT)
            }
        }
    }

    private fun createItem(id: Int, name: String,
                           dimension: String,
                           description: String,
                           amount: Int,
                           unit: String,
                           pricePerUnit: Double): Item {
        val convertedUnit = Unit.fromString(unit)
        val convertedDimension = Dimension.fromString(dimension, convertedUnit)

        return Item(id, name, convertedDimension, description, amount, pricePerUnit)
    }
}