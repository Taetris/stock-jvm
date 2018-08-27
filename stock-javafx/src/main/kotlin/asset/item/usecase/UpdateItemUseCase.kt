package asset.item.usecase

import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.item.ObservableItemRepository
import javax.inject.Inject

class UpdateItemUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(UpdateItemUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    @Throws(UseCaseException::class)
    suspend fun updateItem(id: Int, amount: Int, pricePerUnit: Double) {
        withContext(CommonPool) {
            if (!itemRepository.containsItem(id)) {
                val message = "Item with id '$id' doesn't exist."
                logger.error(message)
                throw UseCaseException(message)
            }

            try {
                val item = itemRepository.getItemById(id)
                val updatedItem = item.copy(amount = amount, pricePerUnit = pricePerUnit)
                itemRepository.updateItem(updatedItem)
            } catch (e: RepositoryException) {
                val message = "Failed to update the item with id '$id'."
                logger.error(message, e)
                throw UseCaseException(message, e)
            }
        }
    }
}