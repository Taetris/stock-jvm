package asset.overview.item.interactor

import repository.item.Item

interface GetAllItemsOutput {

    fun onItemsRetrieved(items: List<Item>)

    fun onRetrievalFailed(error: String)
}