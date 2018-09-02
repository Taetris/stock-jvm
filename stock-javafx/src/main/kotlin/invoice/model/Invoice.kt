package invoice.model

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import repository.customer.Customer
import repository.item.Item

data class Invoice(val invoiceId: Int, val customer: Customer) {

    val selectedItems: ObservableList<Item> = FXCollections.observableArrayList<Item>()
}