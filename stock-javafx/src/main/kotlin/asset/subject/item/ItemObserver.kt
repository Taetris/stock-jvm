package asset.subject.item

/**
 * Observer to get notified when item repository changes.
 */
interface ItemObserver {

    /**
     * Item repository has changed. New items can be retrieved.
     */
    fun onItemsChanged()
}