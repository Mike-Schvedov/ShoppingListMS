package com.mikeschvedov.shoppinglistms.data.mediator

import com.mikeschvedov.shoppinglistms.models.GroceryItem
import kotlinx.coroutines.flow.Flow


interface MediatorProtocol {

    fun saveNewEntry(groceryItem: GroceryItem)

    fun toggleItemMarked(id: String, isMarked: Boolean)

    suspend fun fetchGroceryData() : Flow<List<GroceryItem>>
}