package asset.overview.customer.interactor

import repository.customer.Customer

interface GetAllCustomersOutput {

    fun onCustomersRetrieved(customers: List<Customer>)

    fun onRetrievalFailed(error: String)
}