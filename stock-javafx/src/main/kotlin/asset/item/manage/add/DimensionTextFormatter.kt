package asset.item.manage.add

import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import java.util.function.UnaryOperator

class DimensionTextFormatter : TextFormatter<String>(
        object : StringConverter<String>() {
            override fun toString(`object`: String?): String {
                return `object`.toString()
            }

            override fun fromString(string: String?): String {
                return string.toString()
            }
        },
        "",
        UnaryOperator { value ->
            val newText = value.controlNewText
            return@UnaryOperator if (newText.matches(DIMENSION_REGEX)) {
                value
            } else {
                null
            }
        }) {

    companion object {

        private val DIMENSION_REGEX = Regex("([0-9]*(\\.[0-9]*)?(\\*[0-9]*(\\.[0-9]*)?)?)?")
    }
}