package asset.add.customer.interactor

/**
 * Output of the insertion interactor.
 */
interface AddCustomerOutput {

    /**
     * Notifies that the insertion was successful.
     */
    fun onInsertionSuccessful()

    /**
     * Notifies that there was an error during the insertion of a customer. Error message is passed through [error].
     */
    fun onInsertionFailed(error: String)
}