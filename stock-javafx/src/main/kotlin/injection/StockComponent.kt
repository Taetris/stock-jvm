package injection

import asset.customer.manage.ManageCustomerController
import asset.customer.overview.CustomerOverviewController
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class, SubjectModule::class])
interface StockComponent {

    fun inject(manageCustomerController: ManageCustomerController)

    fun inject(customerOverviewController: CustomerOverviewController)
}