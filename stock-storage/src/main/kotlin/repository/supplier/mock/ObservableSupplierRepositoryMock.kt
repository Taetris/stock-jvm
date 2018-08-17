package repository.supplier.mock

import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.RepositoryObserver
import repository.supplier.ObservableSupplierRepository
import repository.supplier.Supplier

/**
 * Mock implementation of the [ObservableSupplierRepository] that uses an in-memory storage.
 */
class ObservableSupplierRepositoryMock : ObservableSupplierRepository {

    override val observers: MutableList<RepositoryObserver>
        get() = ArrayList()

    private val suppliers: MutableList<Supplier> = ArrayList()
    private val logger = LoggerFactory.getLogger(ObservableSupplierRepositoryMock::class.java)

    override fun insertSupplier(supplier: Supplier) {
        if (!containsSupplier(supplier.id)) {
            suppliers.add(supplier)
            logger.info("Inserted supplier: '{}'", supplier)
            notifyObservers()
        } else {
            throw RepositoryException("Failed to insert supplier '$supplier'.")
        }
    }

    override fun removeSupplier(supplier: Supplier) {
        if (containsSupplier(supplier.id)) {
            suppliers.remove(supplier)
            logger.info("Removed supplier '{}'", supplier)
            notifyObservers()
        } else {
            throw RepositoryException("Failed to remove supplier '$supplier'.")
        }
    }

    override fun updateSupplier(supplier: Supplier) {
        suppliers.forEach { other ->
            if (other.id == supplier.id) {
                suppliers.remove(other)
                suppliers.add(supplier)
                logger.info("Updated supplier: '{}'", supplier)
                notifyObservers()
                return
            }
        }

        throw RepositoryException("Failed to update supplier with the id '${supplier.id}'.")
    }

    override fun containsSupplier(id: Int): Boolean {
        var result = false
        suppliers.forEach { supplier ->
            if (supplier.id == id) {
                result = true
            }
        }

        logger.info("Contains supplier with id '$id': $result")
        return result    }

    override fun getSupplierById(id: Int): Supplier {
        suppliers.forEach { supplier ->
            if (supplier.id == id) {
                return supplier
            }
        }

        throw RepositoryException("No supplier found associated with the id '$id'.")    }

    override fun getAllSuppliers(): List<Supplier> {
        return suppliers
    }
}