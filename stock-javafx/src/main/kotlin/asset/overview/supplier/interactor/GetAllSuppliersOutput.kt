package asset.overview.supplier.interactor

import repository.supplier.Supplier

interface GetAllSuppliersOutput {

    fun onSuppliersRetrieved(suppliers: List<Supplier>)

    fun onRetrievalFailed(error: String)
}