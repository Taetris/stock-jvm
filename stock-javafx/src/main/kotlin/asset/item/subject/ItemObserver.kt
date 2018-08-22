package asset.item.subject

/**
 * Observer to get notified when asset.item repository changes.
 */
interface ItemObserver {

    /**
     * Item repository has changed. New items can be retrieved.
     */
    fun onItemsChanged()
}