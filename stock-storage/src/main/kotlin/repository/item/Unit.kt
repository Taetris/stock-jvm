package repository.item

/**
 * Defines the unit of measure.
 */
enum class Unit(val value: String) {
    /**
     * Meter.
     */
    M("m"),
    /**
     * Square meter.
     */
    M2("m2");

    companion object {

        fun fromString(value: String): Unit {
            values().forEach {
                if (it.value == value.toLowerCase())
                    return it
            }

            throw IllegalArgumentException("Unknown unit type '$value'.")
        }
    }
}