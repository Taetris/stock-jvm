package overview.view.customer

import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import overview.view.Action
import overview.view.TabViewTemplate
import repository.customer.Customer

class CustomerTabTemplate : TabViewTemplate<Customer>(Action.CUSTOMER) {

    override fun populateTable(tableView: TableView<Customer>) {
        tableView.columns.addAll(getColumns())
    }

    override fun getTabName(): String {
        return "Customers"
    }

    override fun getButtonName(): String {
        return "Add Customer"
    }

    override fun getSearchHint(): String {
        return "Search Customers..."
    }
    
    private fun getColumns(): List<TableColumn<Customer, String>> {
        val idColumn = TableColumn<Customer, String>("ID")
        val nameColumn = TableColumn<Customer, String>("Name")
        val accountNumberColumn = TableColumn<Customer, String>("Account Number")
        val addressColumn = TableColumn<Customer, String>("Address")

        return listOf(idColumn, nameColumn, accountNumberColumn, addressColumn)
    }
}