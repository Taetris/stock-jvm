package repository.supplier.mock

import org.slf4j.LoggerFactory
import repository.RepositoryException
import repository.supplier.Supplier
import repository.supplier.SupplierRepository

/**
 * Mock implementation of the [SupplierRepository] that uses an in-memory storage.
 */
class SupplierRepositoryMock : SupplierRepository {

    private val suppliers: MutableList<Supplier> = ArrayList()
    private val logger = LoggerFactory.getLogger(SupplierRepositoryMock::class.java)

    override fun insertSupplier(supplier: Supplier) {
        if (!containsSupplier(supplier.id)) {
            suppliers.add(supplier)
            logger.info("Inserted supplier: '{}'", supplier)
        } else {
            throw RepositoryException("Failed to insert supplier '$supplier'.")
        }
    }

    override fun removeSupplier(supplier: Supplier) {
        if (containsSupplier(supplier.id)) {
            suppliers.remove(supplier)
            logger.info("Removed supplier '{}'", supplier)
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