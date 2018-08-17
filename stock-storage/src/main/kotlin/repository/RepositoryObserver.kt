package repository

/**
 * Observer that gets notified on changes in the repository.
 */
interface RepositoryObserver {

    /**
     * Called when the repository changes. All data should be retrieved again.
     */
    fun onRepositoryChanged()
}