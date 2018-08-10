package overview.tab

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox

abstract class TabView<T> {

    private val table: TableView<T> = TableView()
    private val addButton: Button = Button()
    private val search: TextField = TextField()

    fun generateView(): Tab {
        table.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY

        addButton.text = getButtonName()
        search.promptText = getSearchHint()

        val bottomPane = HBox(addButton, search)
        bottomPane.alignment = Pos.BOTTOM_RIGHT

        val parent = VBox(table, bottomPane)
        VBox.setVgrow(table, Priority.ALWAYS)
        VBox.setVgrow(bottomPane, Priority.NEVER)

        return Tab(getTabName(), parent)
    }

    abstract fun getTabName(): String

    abstract fun getButtonName(): String

    abstract fun getSearchHint(): String
}