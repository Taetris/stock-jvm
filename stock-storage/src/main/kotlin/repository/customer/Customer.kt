package repository.customer

data class Customer(val id: Int, val name: String, val accountNumber: Int, val address: String = "N/A")