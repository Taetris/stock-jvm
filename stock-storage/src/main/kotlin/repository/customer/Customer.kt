package repository.customer

import org.apache.commons.lang3.Validate

/**
 * Data class representing a customer.
 */
data class Customer(val id: Int, val name: String, val idNumber: String, val pdvNumber: String, val address: String) {

    init {
        Validate.inclusiveBetween(0, Int.MAX_VALUE, id)
        Validate.notEmpty(name)
        Validate.notEmpty(address)
        Validate.notEmpty(idNumber)
        Validate.notEmpty(pdvNumber)
    }
}