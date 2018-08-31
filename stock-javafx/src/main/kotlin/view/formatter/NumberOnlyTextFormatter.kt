package view.formatter

import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import java.util.function.UnaryOperator

/**
 * Extended text formatter which allows numbers only. Used to convert the end result to [Int].
 */
class NumberOnlyTextFormatter : TextFormatter<String> (
        object: StringConverter<String>() {

            override fun toString(`object`: String?): String {
                return `object`.toString()
            }

            override fun fromString(string: String?): String {
                return string.toString()
            }
        },
        "",
        UnaryOperator { value ->
            val newText : String = value.controlNewText
            return@UnaryOperator if (newText.matches(NUMBER_ONLY_REGEX)) {
                value
            } else {
                null
            }
        }) {

    companion object {

        private val NUMBER_ONLY_REGEX = Regex("([1-9][0-9]*)?")
    }
}
