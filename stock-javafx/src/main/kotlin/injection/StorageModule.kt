package injection

import dagger.Module
import dagger.Provides
import repository.customer.CustomerRepository
import repository.customer.mock.CustomerRepositoryMock
import repository.supplier.SupplierRepository
import repository.supplier.mock.SupplierRepositoryMock
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideCustomerRepository(): CustomerRepository {
        return CustomerRepositoryMock()
    }

    @Singleton
    @Provides
    fun provideSupplierRepository(): SupplierRepository {
        return SupplierRepositoryMock()
    }
}