package com.mikeschvedov.shoppinglistms.di

import com.mikeschvedov.shoppinglistms.data.mediator.Mediator
import com.mikeschvedov.shoppinglistms.data.mediator.MediatorProtocol
import com.mikeschvedov.shoppinglistms.data.network.ApiManager
import com.mikeschvedov.shoppinglistms.data.network.ApiManagerProtocol
import com.mikeschvedov.shoppinglistms.data.repository.RepositoryProtocol
import com.mikeschvedov.shoppinglistms.data.repository.DatabaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
@Qualifier
annotation class RoomRepository
*/

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfacesModule {

    // -- Mediator -- //
    @Binds
    @Singleton
    abstract fun provideMediator(mediator: Mediator) : MediatorProtocol

    // -- Repositories -- //
    @Binds
    @Singleton
    abstract fun provideDatabaseRepository(databaseRepository: DatabaseRepository) : RepositoryProtocol

    // -- Api Manager -- //
    @Binds
    @Singleton
    abstract fun provideApiManager(apiManager: ApiManager): ApiManagerProtocol

}