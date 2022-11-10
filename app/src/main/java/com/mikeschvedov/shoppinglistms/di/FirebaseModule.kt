package com.mikeschvedov.shoppinglistms.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    const val DATABASE_NAME =
        "https://shoppinglistms-77dce-default-rtdb.firebaseio.com/"

    @Provides
    fun provideFirebaseAuthenticator(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseUser(firebaseAuth: FirebaseAuth): FirebaseUser? = firebaseAuth.currentUser

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(DATABASE_NAME)

}