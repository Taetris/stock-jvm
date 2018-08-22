package injection

import asset.customer.subject.CustomerSubject
import asset.item.subject.ItemSubject
import dagger.Module
import dagger.Provides
import repository.customer.ObservableCustomerRepository
import repository.item.ObservableItemRepository
import javax.inject.Singleton

@Module(includes = [StorageModule::class])
class SubjectModule {

    @Singleton
    @Provides
    fun provideCustomerSubject(customerRepository: ObservableCustomerRepository): CustomerSubject {
        return CustomerSubject(customerRepository)
    }

    @Singleton
    @Provides
    fun provideItemSubject(itemRepository: ObservableItemRepository): ItemSubject {
        return ItemSubject(itemRepository)
    }
}