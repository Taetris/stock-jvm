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
 * Use case for retrieval of a specific item.
 */
class GetItemUseCase @Inject constructor() {

    private var logger = LoggerFactory.getLogger(GetItemUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    /**
     * Gets an item with the given id. In case the item doesn't exist, or the operation fails, a [UseCaseException]
     * will be thrown.
     *
     * Expected error codes:
     * [ErrorCode.NOT_EXIST] - in case the operation fails due to a missing item.
     * [ErrorCode.OPERATION_FAILED] - in case the retrieval of an item fails.
     */
    @Throws(UseCaseException::class)
    suspend fun getItem(id: Int): Item {
        return withContext(CommonPool) {
            logger.info("Retrieving item with id '$id")

            if (!itemRepository.containsItem(id)) {
                logger.error("Item with id '$id' doesn't exist.")
                throw UseCaseException(ErrorCode.NOT_EXIST)
            }

            try {
                return@withContext itemRepository.getItemById(id)
            } catch (e: RepositoryException) {
                val message = "Failed to get item with id '$id'. Error: ${e.message}"
                logger.error(message, e)
                throw UseCaseException(message, ErrorCode.OPERATION_FAILED)
            }
        }
    }
}