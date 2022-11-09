package com.mikeschvedov.shoppinglistms.ui.home

import androidx.lifecycle.ViewModel
import com.mikeschvedov.shoppinglistms.models.GroceryItem

class HomeViewModel: ViewModel() {

    fun saveNewEntry(groceryItem: GroceryItem) {
       println("this is the item: ${groceryItem.name} | ${groceryItem.amount}")
    }

    fun deleteAll() {
        TODO("Not yet implemented")
    }

    fun deleteMarkedItems() {
        TODO("Not yet implemented")
    }
}