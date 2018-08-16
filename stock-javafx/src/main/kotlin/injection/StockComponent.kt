package injection

import asset.add.customer.interactor.AddCustomerInteractor
import asset.add.supplier.interactor.AddSupplierInteractor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class, ExecutorModule::class])
interface StockComponent {

    fun inject(addSupplierInteractor: AddSupplierInteractor)

    fun inject(addCustomerInteractor: AddCustomerInteractor)
}