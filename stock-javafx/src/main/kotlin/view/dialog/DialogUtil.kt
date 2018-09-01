package view.dialog

import javafx.scene.control.Alert
import java.util.*

object DialogUtil {

    fun showErrorDialog(content: String) {
        val bundle = PropertyResourceBundle(DialogUtil::class.java.classLoader.getResource("bundles/default.properties").openStream())
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = bundle.getString("errorTitle")
        alert.headerText = bundle.getString("errorHeader")
        alert.contentText = content
        alert.showAndWait()
    }
}