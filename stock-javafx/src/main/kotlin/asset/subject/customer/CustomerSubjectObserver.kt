package asset.subject.customer

/**
 * Observer to get notified when customer repository changes.
 */
interface CustomerSubjectObserver {

    /**
     * Customer repository has changed. New items can be retrieved.
     */
    fun onCustomersChanged()
}