package com.mikeschvedov.shoppinglistms.di

import com.mikeschvedov.shoppinglistms.data.repository.Repository
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
//
//    @Binds
//    @Singleton
//    abstract fun provideRoomRepository(roomRepositoryIMPL: RoomRepositoryIMPL) : Repository

}