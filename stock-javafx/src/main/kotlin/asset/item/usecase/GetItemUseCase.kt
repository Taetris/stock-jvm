package asset.item.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.item.Item
import repository.item.ObservableItemRepository
import javax.inject.Inject

class GetItemUseCase @Inject constructor() {

    private var logger = LoggerFactory.getLogger(GetItemUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    @Throws(UseCaseException::class)
    suspend fun getItem(id: Int): Item {
        return withContext(CommonPool) {
            if (!itemRepository.containsItem(id)) {
                val message = "Item with id '$id' doesn't exist."
                logger.error(message)
                throw UseCaseException(message)
            }

            try {
                return@withContext itemRepository.getItemById(id)
            } catch (e: RepositoryException) {
                val message = "Failed to get the item with id '$id'."
                logger.error(message, e)
                throw UseCaseException(message, e)
            }
        }
    }
}