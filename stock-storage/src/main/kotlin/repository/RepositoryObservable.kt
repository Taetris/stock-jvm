package repository

/**
 * Observable interface for notifying about changes on the repository.
 */
interface RepositoryObservable {

    /**
     * Adds an observer.
     */
    fun register(repositoryObserver: RepositoryObserver)

    /**
     * Notifies all registered observers.
     */
    fun notifyObservers()
}

