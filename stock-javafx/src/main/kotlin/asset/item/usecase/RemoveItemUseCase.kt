package asset.item.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.item.Item
import repository.item.ObservableItemRepository
import javax.inject.Inject

class RemoveItemUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(RemoveItemUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    @Throws(UseCaseException::class)
    suspend fun removeItem(item: Item) {
        withContext(CommonPool) {
            logger.info("Removing item with the id '${item.id}'")

            try {
                itemRepository.removeItem(item)
            } catch (e: RepositoryException) {
                val message = "Failed to remove the item with id '${item.id}'."
                logger.error(message, e)
                throw UseCaseException(message, e)
            }
        }
    }
}