package asset.item.usecase

import application.usecase.ErrorCode
import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.item.ObservableItemRepository
import javax.inject.Inject

/**
 * Use case for updating of a item.
 */
class UpdateItemUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(UpdateItemUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    /**
     * Updates the item with the same id. If the item doesn't exist, or the update fails,
     * a [UseCaseException] will be thrown.
     *
     * Expected error codes:
     * [ErrorCode.NOT_EXIST] - in case the operation fails due to a missing item.
     * [ErrorCode.OPERATION_FAILED] - in case the update failed.
     */
    @Throws(UseCaseException::class)
    suspend fun updateItem(id: Int, amount: Int, pricePerUnit: Double) {
        withContext(CommonPool) {
            logger.info("Updating item with the id: '$id'")

            if (!itemRepository.containsItem(id)) {
                logger.error("Item with id '$id' doesn't exist.")
                throw UseCaseException(ErrorCode.NOT_EXIST)
            }

            try {
                val item = itemRepository.getItemById(id)
                val updatedItem = item.copy(amount = amount, pricePerUnit = pricePerUnit)
                itemRepository.updateItem(updatedItem)
            } catch (e: RepositoryException) {
                val message = "Failed to update the item with id '$id. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            } catch (e: IllegalArgumentException) {
                val message = "Invalid input parameters. Error: '${e.message}."
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.INVALID_INPUT)
            }
        }
    }
}