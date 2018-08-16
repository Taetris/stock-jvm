package injection

import asset.add.supplier.interactor.AddSupplierInteractor
import dagger.Component
import overview.StockOverviewController
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class, ExecutorModule::class])
interface StockComponent {

    fun inject(stockOverviewController: StockOverviewController)

    fun inject(addSupplierInteractor: AddSupplierInteractor)
}