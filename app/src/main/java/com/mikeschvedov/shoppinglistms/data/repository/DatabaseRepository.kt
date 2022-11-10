package com.mikeschvedov.shoppinglistms.data.repository

import androidx.lifecycle.MutableLiveData
import com.mikeschvedov.shoppinglistms.data.database.remote.FirebaseManager
import com.mikeschvedov.shoppinglistms.interfaces.OnDataChangedListener
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class DatabaseRepository @Inject constructor(
    private val firebaseManager: FirebaseManager
) : RepositoryProtocol {

    override fun saveNewEntry(groceryItem: GroceryItem) {
        firebaseManager.addNewEntry(groceryItem)
    }

    override suspend  fun fetchGroceryData(): Flow<List<GroceryItem>> = callbackFlow {
        var callback  : OnDataChangedListener? = null
        callback = OnDataChangedListener { items -> trySend(items) }
        firebaseManager.readAllItemsFromFirebase (callback)
        awaitClose{ callback = null}
    }


    override fun toggleItemMarked(id: String, isMarked: Boolean){
        //update item as marked
    }

}