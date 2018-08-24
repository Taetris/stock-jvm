package repository.item

import org.apache.commons.lang3.Validate

/**
 * Data class representing a single item.
 */
data class Item(val id: Int, val name: String, val dimension: Dimension,
                val description: String, val amount: Int, val pricePerUnit: Double) {

    companion object {

        private const val DECIMAL_PLACES = 2.0
    }

    init {
        Validate.inclusiveBetween(0, Int.MAX_VALUE, id)
        Validate.notEmpty(name)
        Validate.notEmpty(description)
        Validate.inclusiveBetween(1, Int.MAX_VALUE, amount)
        Validate.inclusiveBetween(1.0, Double.MAX_VALUE, pricePerUnit)
    }

    /**
     * Calculates the quantity of the item.
     */
    fun calculateQuantity(): Double {
        val quantity = dimension.calculateSize() * amount
        return round(quantity)
    }

    /**
     * Calculates the total price of an item for the provided quantity.
     */
    fun calculateTotalPriceWithoutTax(): Double {
        val quantity = calculateQuantity()
        val totalPrice = quantity * pricePerUnit
        return round(totalPrice)
    }

    /**
     * Calculates the total value with tax applied. The tax should be provided as a percentage e.g
     * 20 for 20%, 17 for 17% etc. The calculation will automatically adapt the value to the correct representation.
     *
     * @param tax [Double]: tax value to use for the calculation.
     */
    fun calculateTotalPriceWithTax(tax: Double): Double {
        val total = calculateTotalPriceWithoutTax()
        val taxedTotal = (total * (tax / 100))
        val totalPriceWithTax = total + taxedTotal
        return round(totalPriceWithTax)
    }

    private fun round(value: Double): Double {
        val factor = Math.pow(10.0, DECIMAL_PLACES)
        return Math.round(value * factor) / factor
    }
}