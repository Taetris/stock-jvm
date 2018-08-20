package asset.add.item.interactor

/**
 * Output of the insertion interactor.
 */
interface AddItemOutput {

    /**
     * Notifies that the insertion was successful.
     */
    fun onInsertionSuccessful()

    /**
     * Notifies that there was an error during the insertion of an item. Error message is passed through [error].
     */
    fun onInsertionFailed(error: String)
}