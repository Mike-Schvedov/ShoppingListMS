package com.mikeschvedov.shoppinglistms.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DBName = "NotesDB"

//    @Provides
//    fun provideAppDatabase(@ApplicationContext appContext: Context) =
//        Room.databaseBuilder(appContext, AppDatabase::class.java, DBName)
//            .build()
//
//
//    @Provides
//    fun provideNotesDao(appDatabase: AppDatabase) =
//        appDatabase.notesDao()
//


}