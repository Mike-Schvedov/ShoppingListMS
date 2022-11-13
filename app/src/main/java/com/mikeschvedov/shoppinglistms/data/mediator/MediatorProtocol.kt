package com.mikeschvedov.shoppinglistms.data.mediator

import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.models.User
import kotlinx.coroutines.flow.Flow


interface MediatorProtocol {

    // ---------------------- Shopping Lists ---------------------- //
    fun saveNewEntry(groceryItem: GroceryItem)

    fun toggleItemMarked(item: GroceryItem, isMarked: Boolean)

    suspend fun fetchGroceryData() : Flow<List<GroceryItem>>

    // ---------------------- Users ---------------------- //
    fun addUserToDatabase(user: User)

    suspend fun getUserConnectedShoppingListID(): Flow<String>

    // ----------------- Code ------------------ //
    suspend fun getAllValidInviteCodes(): Flow<List<String>>
}