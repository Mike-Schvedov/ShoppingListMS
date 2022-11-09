package com.mikeschvedov.shoppinglistms.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirebaseAuthenticator(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseUser(firebaseAuth: FirebaseAuth): FirebaseUser? = firebaseAuth.currentUser

}