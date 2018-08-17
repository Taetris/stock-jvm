package asset.subject.supplier

/**
 * Observer to get notified when supplier repository changes.
 */
interface SupplierSubjectObserver {

    /**
     * Supplier repository has changed. New items can be retrieved.
     */
    fun onSuppliersChanged()
}