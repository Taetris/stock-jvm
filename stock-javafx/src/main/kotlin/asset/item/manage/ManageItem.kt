package asset.item.manage

import javafx.scene.Scene
import javafx.scene.control.TextInputDialog

class ManageItem {

    companion object {

        fun createView(): Scene? {
            val dialog = TextInputDialog()
            dialog.headerText = "Upišite ID robe. Ukoliko roba sa datim ID-om postoji,\notvorit će se prozor za izmjenu iste."
            dialog.contentText = "ID"

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