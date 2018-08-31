package asset.item.manage

import javafx.scene.Scene
import javafx.scene.control.TextInputDialog
import view.formatter.TextToIntFormatter
import java.util.*

class ManageItem {

    companion object {

        fun createView(): Scene? {
            val bundle = PropertyResourceBundle(ManageItem::class.java.classLoader.getResource("bundles/default.properties").openStream())
            val dialog = TextInputDialog()
            dialog.headerText = bundle.getString("manageItemDialogHeader")
            dialog.contentText = bundle.getString("manageItemDialogContent")
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