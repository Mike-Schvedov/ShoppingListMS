package com.mikeschvedov.shoppinglistms.data.repository

import com.mikeschvedov.shoppinglistms.data.database.remote.FirebaseManager
import com.mikeschvedov.shoppinglistms.interfaces.OnInviteCodeChangedListener
import com.mikeschvedov.shoppinglistms.interfaces.OnGroceryItemChangedListener
import com.mikeschvedov.shoppinglistms.interfaces.OnStringChangedListener
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.models.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class DatabaseRepository @Inject constructor(
    private val firebaseManager: FirebaseManager
) : RepositoryProtocol {

    // ---------------------- Shopping Lists ---------------------- //
    override fun saveNewEntry(groceryItem: GroceryItem) {
        firebaseManager.addNewEntry(groceryItem)
    }

    override suspend  fun fetchGroceryData(): Flow<List<GroceryItem>> = callbackFlow {
        println("fetchGroceryData() - Repository")
        var callback  : OnGroceryItemChangedListener?
        callback = OnGroceryItemChangedListener { items -> trySend(items) }
        firebaseManager.readAllItemsFromFirebase (callback)
        awaitClose{ callback = null}
    }

    override fun toggleItemMarked(item: GroceryItem, isMarked: Boolean){
        firebaseManager.updateItemIsMarked(item, isMarked)
    }

    // ---------------------- Users ---------------------- //
    override fun addUserToDatabase(user: User){
        firebaseManager.createNewUser(user)
    }

    override suspend fun getUserConnectedShoppingListID() : Flow<String> = callbackFlow {
        println("getUserConnectedShoppingListID() - Repository")
        var callback  : OnStringChangedListener?
        callback = OnStringChangedListener { id -> trySend(id) }
        firebaseManager.getUserConnectedShoppingListID (callback)
        awaitClose{ callback = null}
    }

    // ----------------------- Code --------------------- //
    override suspend fun getAllValidInviteCodes() : Flow<List<String>> = callbackFlow {
        println("Entered the repository")
        var callback  : OnInviteCodeChangedListener?
        callback = OnInviteCodeChangedListener { list ->
            println("Returning from the repository : $list")
            trySend(list) }
        firebaseManager.getAllValidInviteCodes (callback)
        awaitClose{ callback = null}
    }
}