package repository.invoice.mock

import repository.invoice.InvoiceRepository

/**
 * Mock implementation of the [InvoiceRepository].
 */
class InvoiceRepositoryMock : InvoiceRepository {

    private var wholesaleId: Int = 0
    private var retailId: Int = 0

    override fun getNextInvoiceIdForWholesale(): Int = ++wholesaleId

    override fun getNextInvoiceIdForRetail(): Int = ++retailId
}