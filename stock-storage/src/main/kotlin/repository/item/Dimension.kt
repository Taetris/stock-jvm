package repository.item

import org.apache.commons.lang3.Validate

class Dimension(val width: Double, val height: Double, val unit: Unit) {

    init {
        Validate.inclusiveBetween(0.0, Double.MAX_VALUE, width)
        Validate.inclusiveBetween(0.0, Double.MAX_VALUE, height)

        when (unit) {
            Unit.M -> Validate.isTrue(height == 1.0)
            else -> {
            }
        }
    }

    fun calculateSize(): Double {
        return when (unit) {
            Unit.M -> width
            Unit.M2 -> width * height
        }
    }

    override fun toString(): String {
        return "$width * $height"
    }
}