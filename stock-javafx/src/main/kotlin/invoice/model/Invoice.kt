package invoice.model

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import repository.customer.Customer
import repository.item.Item
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

data class Invoice(val invoiceId: Int, val customer: Customer) {

    val localDate: LocalDate

    init {
        val date = Date()
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    // TODO: Make private, add access methods, change to List interface
    val selectedItems: ObservableList<Item> = FXCollections.observableArrayList<Item>()
}