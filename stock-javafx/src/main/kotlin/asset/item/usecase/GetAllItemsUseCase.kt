package asset.item.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.item.Item
import repository.item.ObservableItemRepository
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(GetAllItemsUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    @Throws(UseCaseException::class)
    suspend fun getAllItems(): List<Item> {
        return withContext(CommonPool) {
            logger.info("Getting all items")

            try {
                return@withContext itemRepository.getAllItems()
            } catch (e: RepositoryException) {
                val message = "Failed to retrieve all items."
                logger.error(message, e)
                throw UseCaseException(message, e)
            }
        }
    }
}