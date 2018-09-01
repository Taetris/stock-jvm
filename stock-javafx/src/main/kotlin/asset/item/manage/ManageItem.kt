package asset.item.manage

import application.ResourceLoader
import javafx.scene.Scene
import javafx.scene.control.TextInputDialog
import view.formatter.TextToIntFormatter

class ManageItem {

    companion object {

        fun createView(): Scene? {
            val dialog = TextInputDialog()
            dialog.headerText = ResourceLoader.bundle.getString("manageItemDialogHeader")
            dialog.contentText = ResourceLoader.bundle.getString("manageItemDialogContent")
            dialog.editor.textFormatter = TextToIntFormatter()

            val result = dialog.showAndWait()
            return if (result.isPresent) {
                ManageItemController.create(result.get().toInt())
            } else {
                dialog.close()
                null
            }
        }
    }
}