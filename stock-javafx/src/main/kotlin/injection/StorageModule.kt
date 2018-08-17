package injection

import dagger.Module
import dagger.Provides
import repository.customer.ObservableCustomerRepository
import repository.customer.mock.ObservableCustomerRepositoryMock
import repository.supplier.ObservableSupplierRepository
import repository.supplier.mock.ObservableSupplierRepositoryMock
import javax.inject.Singleton

@Module
internal class StorageModule {

    @Singleton
    @Provides
    fun provideCustomerRepository(): ObservableCustomerRepository {
        return ObservableCustomerRepositoryMock()
    }

    @Singleton
    @Provides
    fun provideSupplierRepository(): ObservableSupplierRepository {
        return ObservableSupplierRepositoryMock()
    }
}