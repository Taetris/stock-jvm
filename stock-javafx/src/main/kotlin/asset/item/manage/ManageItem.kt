package asset.item.manage

import asset.item.manage.add.AddItemController
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType

class ManageItem {

    companion object {

        fun createView(): Scene {
            val alert = Alert(Alert.AlertType.CONFIRMATION)
            alert.title = "Unos Robe"
            alert.headerText = "Unos Robe"
            alert.contentText = "Da li želite unijeti novu ili postojeću robu?"

            val addNewItemButton = ButtonType("Nova")
            val editExistingItemButton = ButtonType("Postojeća")
            alert.buttonTypes.setAll(addNewItemButton, editExistingItemButton)

            val result = alert.showAndWait()
            if (result.get() == addNewItemButton) {
                return AddItemController.create()
            } else {
                return AddItemController.create()
            }
        }
    }
}