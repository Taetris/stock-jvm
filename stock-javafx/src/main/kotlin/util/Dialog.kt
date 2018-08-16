package util

import javafx.scene.control.Alert

object Dialog {

    fun showErrorDialog(title: String = "Error", header: String, content: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = title
        alert.headerText = header
        alert.contentText = content
        alert.showAndWait()
    }
}