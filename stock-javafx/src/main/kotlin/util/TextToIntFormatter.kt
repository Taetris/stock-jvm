package util

import javafx.scene.control.TextFormatter
import javafx.util.converter.IntegerStringConverter
import java.util.function.UnaryOperator

/**
 * Extended text formatter which allows numbers only. Used to convert the end result to [Int].
 */
class TextToIntFormatter : TextFormatter<Int>(
        IntegerStringConverter(),
        0,
        UnaryOperator { value ->
            val newText : String = value.controlNewText
            return@UnaryOperator if (newText.matches(NUMBER_ONLY_REGEX)) {
                value
            } else {
                null
            }
        }) {

    companion object {

        private val NUMBER_ONLY_REGEX = Regex("-?([1-9][0-9]*)?")
    }
}