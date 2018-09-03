package view.dialog

import application.ResourceLoader
import javafx.scene.control.Alert

object DialogUtil {

    fun showErrorDialog(content: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = ResourceLoader.bundle.getString("errorTitle")
        alert.headerText = ResourceLoader.bundle.getString("errorHeader")
        alert.contentText = content
        alert.showAndWait()
    }
}