package overview.view

import asset.add.AddAssetController
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.slf4j.LoggerFactory

/**
 * Generic view which contains specific information e.g customers, suppliers etc. This class is built based on the
 * template pattern. Each specific implementation should extend the implementation and only implement the abstract
 * defined methods. The generation of the view is being done by the super class.
 */
abstract class TabViewTemplate<T>(private val action: Action) {

    private val logger = LoggerFactory.getLogger(TabViewTemplate::class.java)

    private var table: TableView<T> = TableView()
    private val addButton: Button = Button()
    private val search: TextField = TextField()

    /**
     * Generates the view view.
     */
    fun generateView(): Tab {
        logger.info("Generating Tab")

        setListeners()
        applyCustomViewChanges()

        return Tab(getTabName(), buildRootPane())
    }

    protected abstract fun populateTable(tableView: TableView<T>)

    protected abstract fun getTabName(): String

    protected abstract fun getButtonName(): String

    protected abstract fun getSearchHint(): String

    private fun setListeners() {
        addButton.setOnAction {
            val stage = Stage()
            stage.scene = AddAssetController.create(action)
            stage.show()
        }
    }

    private fun applyCustomViewChanges() {
        table.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
        populateTable(table)

        addButton.text = getButtonName()
        search.promptText = getSearchHint()
    }

    private fun buildRootPane(): Pane {
        val bottomPane = HBox(addButton, search)
        bottomPane.alignment = Pos.BOTTOM_RIGHT
        bottomPane.padding = Insets(5.0, 5.0, 5.0, 0.0)

        val parent = VBox(table, bottomPane)
        VBox.setVgrow(table, Priority.ALWAYS)
        VBox.setVgrow(bottomPane, Priority.NEVER)

        return parent
    }
}