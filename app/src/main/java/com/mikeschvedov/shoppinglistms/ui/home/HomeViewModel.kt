package com.mikeschvedov.shoppinglistms.ui.home

import androidx.lifecycle.ViewModel
import com.mikeschvedov.shoppinglistms.data.repository.Repository
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
    ): ViewModel() {

    fun saveNewEntry(groceryItem: GroceryItem) {
      repository.saveNewEntry(groceryItem)
    }

    fun deleteAll() {
        TODO("Not yet implemented")
    }

    fun deleteMarkedItems() {
        TODO("Not yet implemented")
    }
}