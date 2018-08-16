package repository.customer

import org.apache.commons.lang3.Validate

data class Customer(val id: Int, val name: String, val accountNumber: String, val address: String = "N/A") {

    init {
        Validate.inclusiveBetween(0, Int.MAX_VALUE, id)
        Validate.notEmpty(name)
        Validate.notEmpty(accountNumber)
    }
}