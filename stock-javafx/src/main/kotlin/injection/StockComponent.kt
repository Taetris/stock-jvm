package injection

import asset.add.customer.interactor.AddCustomerInteractor
import asset.add.item.interactor.AddItemInteractor
import asset.overview.customer.interactor.GetAllCustomersInteractor
import asset.overview.customer.interactor.RemoveCustomerInteractor
import asset.overview.item.interactor.GetAllItemsInteractor
import asset.subject.customer.CustomerSubject
import asset.subject.item.ItemSubject
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class, ExecutorModule::class])
interface StockComponent {

    fun inject(addItemInteractor: AddItemInteractor)

    fun inject(addCustomerInteractor: AddCustomerInteractor)

    fun inject(getAllCustomersInteractor: GetAllCustomersInteractor)

    fun inject(getAllItemsInteractor: GetAllItemsInteractor)

    fun inject(customerSubject: CustomerSubject)

    fun inject(itemSubject: ItemSubject)

    fun inject(removeCustomerInteractor: RemoveCustomerInteractor)
}