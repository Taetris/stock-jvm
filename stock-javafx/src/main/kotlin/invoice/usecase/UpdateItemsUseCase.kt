package invoice.usecase

import application.usecase.ErrorCode
import application.usecase.UseCaseException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.item.Item
import repository.item.ObservableItemRepository
import javax.inject.Inject

class UpdateItemsUseCase @Inject constructor() {

    private val logger = LoggerFactory.getLogger(UpdateItemsUseCase::class.java)

    @Inject
    internal lateinit var itemRepository: ObservableItemRepository

    @Throws(UseCaseException::class)
    suspend fun updateItems(items: List<Item>) {
        withContext(CommonPool) {
            items.forEach { updateItemWithReducedAmount(it.id, it.amount) }
        }
    }

    private fun updateItemWithReducedAmount(id: Int, amount: Int) {
        logger.info("Updating item with the id: '$id'")

        if (!itemRepository.containsItem(id)) {
            logger.error("Item with id '$id' doesn't exist.")
            throw UseCaseException(ErrorCode.NOT_EXIST)
        }

        try {
            val item = itemRepository.getItemById(id)
            val newAmount = reduceAmount(item.amount, amount)
            val updatedItem = item.copy(amount = newAmount)
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

    private fun reduceAmount(currentAmount: Int, amountTaken: Int) = currentAmount - amountTaken
}