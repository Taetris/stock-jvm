package injection

import application.StockApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class])
interface StockComponent {

    fun inject(stockApplication: StockApplication)
}