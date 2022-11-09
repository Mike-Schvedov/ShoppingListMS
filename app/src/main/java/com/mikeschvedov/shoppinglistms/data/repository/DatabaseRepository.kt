package com.mikeschvedov.shoppinglistms.data.repository

import androidx.lifecycle.MutableLiveData
import com.mikeschvedov.shoppinglistms.data.database.remote.FirebaseManager
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


class DatabaseRepository @Inject constructor(
    private val firebaseManager: FirebaseManager
) : RepositoryProtocol {

    override fun saveNewEntry(groceryItem: GroceryItem) {
        println("this is the item inside the repo: ${groceryItem.name} | ${groceryItem.amount}")
    }

    override fun fetchGroceryData(): Flow<List<GroceryItem>>
    {
    return flowOf()
    }

    override fun toggleItemMarked(id: String, isMarked: Boolean){
        //update item as marked
    }

}