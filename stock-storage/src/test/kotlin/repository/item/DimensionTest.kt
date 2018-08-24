package repository.item

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class DimensionTest {

    companion object {

        private const val WIDTH = 1.0
        private const val HEIGHT = 1.0
        private val UNIT = Unit.M2

        private fun createDummyDimension(width: Double = WIDTH, height: Double = HEIGHT, unit: Unit = UNIT): Dimension {
            return Dimension(width, height, unit)
        }
    }

    @Test
    fun shouldInitFailWithNegativeWidth() {
        assertThrows(IllegalArgumentException::class.java) { createDummyDimension(width = -1.0) }
    }

    @Test
    fun shouldInitFailWithNegativeHeight() {
        assertThrows(IllegalArgumentException::class.java) { createDummyDimension(height = -1.0) }
    }

    @Test
    fun shouldFailIfHeightIsNotOneForM() {
        assertThrows(IllegalArgumentException::class.java) { createDummyDimension(height = 5.0, unit = Unit.M) }
    }

    @Test
    fun shouldCalculateCorrectSizeForM() {
        val dimension = createDummyDimension(width = 5.0, height = 1.0, unit = Unit.M)
        assertThat(dimension.calculateSize()).isEqualTo(5.0)
    }

    @Test
    fun shouldCalculateCorrectSizeForM2() {
        val dimension = createDummyDimension(width = 5.0, height = 3.0, unit = Unit.M2)
        assertThat(dimension.calculateSize()).isEqualTo(15.0)
    }
}