package injection

import dagger.Component
import overview.StockOverviewController
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class])
interface StockComponent {

    fun inject(stockOverviewController: StockOverviewController)
}