package view.formatter

import javafx.scene.control.TextFormatter
import javafx.util.converter.DoubleStringConverter
import java.util.function.UnaryOperator

/**
 * Extended text formatter which allows numbers only. Used to convert the end result to [Int].
 */
class TextToDoubleFormatter : TextFormatter<Double>(
        DoubleStringConverter(),
        0.0,
        UnaryOperator { value ->
            val newText : String = value.controlNewText
            return@UnaryOperator if (newText.matches(NUMBER_ONLY_REGEX)) {
                value
            } else {
                null
            }
        }) {

    companion object {

        private val NUMBER_ONLY_REGEX = Regex("([0-9]*(\\.[0-9]*)?)?")
    }
}