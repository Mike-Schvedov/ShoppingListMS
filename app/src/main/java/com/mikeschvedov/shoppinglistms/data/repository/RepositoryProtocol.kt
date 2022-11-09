package com.mikeschvedov.shoppinglistms.data.repository

import androidx.lifecycle.MutableLiveData
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import kotlinx.coroutines.flow.Flow

interface RepositoryProtocol  {


    fun saveNewEntry(groceryItem: GroceryItem)

    fun fetchGroceryData(): Flow<List<GroceryItem>>

    fun toggleItemMarked(id: String, isMarked: Boolean)

}