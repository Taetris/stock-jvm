package overview.tab

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.slf4j.LoggerFactory

abstract class TabView<T> {

    private val logger = LoggerFactory.getLogger(TabView::class.java)

    private var table: TableView<T> = TableView()
    private val addButton: Button = Button()
    private val search: TextField = TextField()

    fun generateView(): Tab {
        logger.info("Generating Tab")

        applyCustomViewChanges()

        return Tab(getTabName(), buildRootPane())
    }

    protected abstract fun populateTable(tableView: TableView<T>)

    protected abstract fun getTabName(): String

    protected abstract fun getButtonName(): String

    protected abstract fun getSearchHint(): String

    private fun applyCustomViewChanges() {
        table.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
        populateTable(table)

        addButton.text = getButtonName()
        search.promptText = getSearchHint()
    }

    private fun buildRootPane(): Pane {
        val bottomPane = HBox(addButton, search)
        bottomPane.alignment = Pos.BOTTOM_RIGHT

        val parent = VBox(table, bottomPane)
        VBox.setVgrow(table, Priority.ALWAYS)
        VBox.setVgrow(bottomPane, Priority.NEVER)

        return parent
    }
}