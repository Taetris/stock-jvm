package repository

/**
 * Observable interface for notifying about changes on the repository.
 */
interface RepositoryObservable {

    // TODO: Current approach, breaks encapsulation. Should be refactored.
    val observers: MutableList<RepositoryObserver>

    /**
     * Adds an observer.
     */
    fun register(repositoryObserver: RepositoryObserver) = observers.add(repositoryObserver)

    /**
     * Removes an observer.
     */
    fun unregister(repositoryObserver: RepositoryObserver) = observers.remove(repositoryObserver)

    /**
     * Notifies all registered observers.
     */
    fun notifyObservers() = observers.forEach { observer -> observer.onRepositoryChanged() }
}

