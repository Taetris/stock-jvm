package asset.add.supplier.interactor

/**
 * Output of the insertion interactor.
 */
interface AddSupplierOutput {

    /**
     * Notifies that the insertion was successful.
     */
    fun onInsertionSuccessful()

    /**
     * Notifies that there was an error during the insertion of a supplier. Error message is passed through [error].
     */
    fun onInsertionFailed(error: String)
}