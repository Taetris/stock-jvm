package repository.item

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

/**
 * Tests the initialization and functionality of an [Item].
 */
internal class ItemTest {

    companion object {

        private const val ID = 1
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val AMOUNT = 1
        private const val PRICE = 1.0
        private val DIMENSION = createDummyDimension(1.0, 1.0)
        private const val TAX = 17.0

        private fun createDummyItem(id: Int = ID, name: String = NAME, dimension: Dimension = DIMENSION,
                                    description: String = DESCRIPTION, amount: Int = AMOUNT, pricePerUnit: Double = PRICE): Item {
            return Item(id, name, dimension, description, amount, pricePerUnit)
        }

        private fun createDummyDimension(width: Double, height: Double): Dimension {
            return Dimension(width, height, Unit.M2)
        }
    }

    @Test
    fun shouldInitFailWithNegativeId() {
        assertThrows(IllegalArgumentException::class.java) { createDummyItem(id = -1) }
    }

    @Test
    fun shouldInitFailWithEmptyNameAndDescription() {
        assertThrows(IllegalArgumentException::class.java) { createDummyItem(name = "", description = "") }
    }

    @Test
    fun shouldInitFailWithZeroOrNegativeAmount() {
        assertThrows(IllegalArgumentException::class.java) { createDummyItem(amount = 0) }
        assertThrows(IllegalArgumentException::class.java) { createDummyItem(amount = -1) }
    }

    @Test
    fun shouldInitFailWithZeroOrNegativePrice() {
        assertThrows(IllegalArgumentException::class.java) { createDummyItem(pricePerUnit = 0.0) }
        assertThrows(IllegalArgumentException::class.java) { createDummyItem(pricePerUnit = -1.0) }
    }

    @Test
    fun shouldCorrectlyCalculateQuantity() {
        var dimension = createDummyDimension(25.0, 0.6)
        var item = createDummyItem(dimension = dimension, amount = 1, pricePerUnit = 19.00)
        assertThat(item.calculateQuantity()).isEqualTo(15.00)

        dimension = createDummyDimension(1.4, 2.0)
        item = createDummyItem(dimension = dimension, amount = 4, pricePerUnit = 19.00)
        assertThat(item.calculateQuantity()).isEqualTo(11.20)

        dimension = createDummyDimension(1.5, 2.3)
        item = createDummyItem(dimension = dimension, amount = 4, pricePerUnit = 19.00)
        assertThat(item.calculateQuantity()).isEqualTo(13.80)

        dimension = createDummyDimension(2.0, 3.0)
        item = createDummyItem(dimension = dimension, amount = 4, pricePerUnit = 19.00)
        assertThat(item.calculateQuantity()).isEqualTo(24.00)
    }

    @Test
    fun shouldCorrectlyCalculateTotalPriceWithoutTax() {
        var dimension = createDummyDimension(25.0, 0.6)
        var item = createDummyItem(dimension = dimension, amount = 1, pricePerUnit = 19.00)
        assertThat(item.calculateTotalPriceWithoutTax()).isEqualTo(285.00)

        dimension = createDummyDimension(1.4, 2.0)
        item = createDummyItem(dimension = dimension, amount = 4, pricePerUnit = 19.00)
        assertThat(item.calculateTotalPriceWithoutTax()).isEqualTo(212.80)

        dimension = createDummyDimension(1.5, 2.3)
        item = createDummyItem(dimension = dimension, amount = 4, pricePerUnit = 19.00)
        assertThat(item.calculateTotalPriceWithoutTax()).isEqualTo(262.20)

        dimension = createDummyDimension(2.0, 3.0)
        item = createDummyItem(dimension = dimension, amount = 4, pricePerUnit = 19.00)
        assertThat(item.calculateTotalPriceWithoutTax()).isEqualTo(456.00)
    }

    @Test
    fun shouldCorrectlyCalculateTotalPriceWithTax() {
        var dimension = createDummyDimension(25.0, 0.6)
        var item = createDummyItem(dimension = dimension, amount = 1, pricePerUnit = 19.00)
        assertThat(item.calculateTotalPriceWithTax(TAX)).isEqualTo(333.45)

        dimension = createDummyDimension(1.4, 2.0)
        item = createDummyItem(dimension = dimension, amount = 4, pricePerUnit = 19.00)
        assertThat(item.calculateTotalPriceWithTax(TAX)).isEqualTo(248.98)

        dimension = createDummyDimension(1.5, 2.3)
        item = createDummyItem(dimension = dimension, amount = 4, pricePerUnit = 19.00)
        assertThat(item.calculateTotalPriceWithTax(TAX)).isEqualTo(306.77)

        dimension = createDummyDimension(2.0, 3.0)
        item = createDummyItem(dimension = dimension, amount = 4, pricePerUnit = 19.00)
        assertThat(item.calculateTotalPriceWithTax(TAX)).isEqualTo(533.52)
    }
}