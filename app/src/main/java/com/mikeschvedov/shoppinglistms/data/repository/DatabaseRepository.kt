package com.mikeschvedov.shoppinglistms.data.repository

import com.mikeschvedov.shoppinglistms.data.database.remote.FirebaseManager
import com.mikeschvedov.shoppinglistms.data.network.ApiManagerProtocol
import com.mikeschvedov.shoppinglistms.data.network.models.PushNotification
import com.mikeschvedov.shoppinglistms.interfaces.OnInviteCodeChangedListener
import com.mikeschvedov.shoppinglistms.interfaces.OnGroceryItemChangedListener
import com.mikeschvedov.shoppinglistms.interfaces.OnStringChangedListener
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.models.User
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import com.mikeschvedov.shoppinglistms.util.utility_models.Failure
import com.mikeschvedov.shoppinglistms.util.utility_models.Success
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class DatabaseRepository @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val apiManagerImpl: ApiManagerProtocol
) : RepositoryProtocol {

    // ---------------------- Shopping Lists ---------------------- //
    override fun saveNewEntry(groceryItem: GroceryItem) {
        firebaseManager.addNewEntry(groceryItem)
    }

    override suspend fun fetchGroceryData(): Flow<List<GroceryItem>> = callbackFlow {
        LoggerService.info("fetchGroceryData() - Repository")
        var callback: OnGroceryItemChangedListener?
        callback = OnGroceryItemChangedListener { items -> trySend(items) }
        firebaseManager.readAllItemsFromFirebase(callback)
        awaitClose { callback = null }
    }

    override fun toggleItemMarked(item: GroceryItem, isMarked: Boolean) {
        firebaseManager.updateItemIsMarked(item, isMarked)
    }

    override suspend fun deleteAll() {
        firebaseManager.deleteAll()
    }

    override suspend fun deleteMarkedItems() {
        firebaseManager.deleteMarkedItems()
    }

    // ---------------------- Users ---------------------- //
    override fun addUserToDatabase(user: User) {
        firebaseManager.createNewUser(user)
    }

    override suspend fun getUserConnectedShoppingListID(): Flow<String> = callbackFlow {
        println("getUserConnectedShoppingListID() - Repository")
        var callback: OnStringChangedListener?
        callback = OnStringChangedListener { id -> trySend(id) }
        firebaseManager.getUserConnectedShoppingListID(callback)
        awaitClose { callback = null }
    }

    // ----------------------- Code --------------------- //
    override suspend fun getAllValidInviteCodes(): Flow<List<String>> = callbackFlow {
        println("Entered the repository")
        var callback: OnInviteCodeChangedListener?
        callback = OnInviteCodeChangedListener { list ->
            println("Returning from the repository : $list")
            trySend(list)
        }
        firebaseManager.getAllValidInviteCodes(callback)
        awaitClose { callback = null }
    }

    override suspend fun sendNotification(it: PushNotification) {
        val responseBody = apiManagerImpl.postNotification(it)
        if (responseBody is Success) {
            val withoutWrapper = responseBody.data
            LoggerService.info("Response from notification server - Success: Response: ${withoutWrapper.message()}")
        } else if (responseBody is Failure) {
            LoggerService.info("Response from notification server - Failure: " + responseBody.exc?.message)
        }
    }
}