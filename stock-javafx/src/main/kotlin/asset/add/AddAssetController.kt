package asset.add

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import overview.view.Action
import overview.view.Action.CUSTOMER
import overview.view.Action.SUPPLIER

class AddAssetController {

    @FXML
    lateinit var rootPane: AnchorPane

    companion object {

        fun create(action: Action): Scene {
            val view = FXMLLoader.load<AnchorPane>(AddAssetController::class.java.getResource("../../../resources/asset/stock-add-asset.fxml"))

            val childView: Pane = when (action) {
                CUSTOMER -> AddCustomerController.createView()
                SUPPLIER -> AddSupplierController.createView()
            }

            view.children.clear()
            view.children.add(childView)

            return Scene(view)
        }
    }
}