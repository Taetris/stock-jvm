package asset.item.usecase

import application.usecase.ErrorCode
import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.item.Item
import repository.item.ObservableItemRepository
import javax.inject.Inject

/**
 * Use case for a removal of an item.
 */
class RemoveItemUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(RemoveItemUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    /**
     * Removes an item from the repository. If the item doesn't exist, or the removal fails,
     * a [UseCaseException] will be thrown.
     *
     * Expected error codes:
     * [ErrorCode.NOT_EXIST] - in case the operation fails due to a missing item.
     * [ErrorCode.OPERATION_FAILED] - in case the removal failed.
     */
    @Throws(UseCaseException::class)
    suspend fun removeItem(item: Item) {
        withContext(CommonPool) {
            logger.info("Removing item with the id '${item.id}'")

            if (!itemRepository.containsItem(item.id)) {
                logger.error("Item with id '${item.id}' doesn't exist.")
                throw UseCaseException(ErrorCode.NOT_EXIST)
            }

            try {
                itemRepository.removeItem(item)
            } catch (e: RepositoryException) {
                val message = "Failed to remove the item with id '${item.id}. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            }
        }
    }
}