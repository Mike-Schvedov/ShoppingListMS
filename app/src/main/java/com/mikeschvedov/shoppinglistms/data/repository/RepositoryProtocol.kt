package com.mikeschvedov.shoppinglistms.data.repository

import com.mikeschvedov.shoppinglistms.data.network.models.PushNotification
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.models.User
import kotlinx.coroutines.flow.Flow

interface RepositoryProtocol  {

    // ---------------------- Shopping Lists ---------------------- //
    fun saveNewEntry(groceryItem: GroceryItem)

    suspend fun fetchGroceryData(): Flow<List<GroceryItem>>

    fun toggleItemMarked(item: GroceryItem, isMarked: Boolean)

    suspend fun deleteAll()

    suspend fun deleteMarkedItems()

    // ---------------------- Users ---------------------- //
    fun addUserToDatabase(user: User)

    suspend fun getUserConnectedShoppingListID(): Flow<String>

    // ----------------------- Code ---------------------- //
    suspend fun getAllValidInviteCodes() : Flow<List<String>>


    suspend fun sendNotification(it: PushNotification)
}