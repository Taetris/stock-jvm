package injection

import asset.customer.manage.ManageCustomerController
import asset.customer.overview.CustomerOverviewController
import asset.item.manage.ManageItemController
import asset.item.overview.ItemOverviewController
import dagger.Component
import invoice.metadata.InvoiceMetadataController
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class, SubjectModule::class])
interface StockComponent {

    fun inject(manageCustomerController: ManageCustomerController)

    fun inject(customerOverviewController: CustomerOverviewController)

    fun inject(itemOverviewController: ItemOverviewController)

    fun inject(manageItemController: ManageItemController)

    fun inject(invoiceMetadataController: InvoiceMetadataController)
}