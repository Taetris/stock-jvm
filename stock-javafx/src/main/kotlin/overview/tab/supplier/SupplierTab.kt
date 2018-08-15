package overview.tab.supplier

import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import overview.tab.TabView
import repository.supplier.Supplier

class SupplierTab : TabView<Supplier>() {

    override fun populateTable(tableView: TableView<Supplier>) {
        tableView.columns.addAll(getColumns())
    }

    override fun getTabName(): String {
        return "Suppliers"
    }

    override fun getButtonName(): String {
        return "Add Supplier"
    }

    override fun getSearchHint(): String {
        return "Search Suppliers..."
    }

    private fun getColumns(): List<TableColumn<Supplier, String>> {
        val idColumn = TableColumn<Supplier, String>("ID")
        val nameColumn = TableColumn<Supplier, String>("Name")
        val accountNumberColumn = TableColumn<Supplier, String>("Account Number")
        val addressColumn = TableColumn<Supplier, String>("Address")

        return listOf(idColumn, nameColumn, accountNumberColumn, addressColumn)
    }
}