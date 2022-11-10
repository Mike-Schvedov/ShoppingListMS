package com.mikeschvedov.shoppinglistms.data.mediator

import androidx.lifecycle.MutableLiveData
import com.mikeschvedov.shoppinglistms.data.repository.RepositoryProtocol
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Mediator @Inject constructor(
    private val databaseRepository: RepositoryProtocol
): MediatorProtocol {

    override fun saveNewEntry(groceryItem: GroceryItem) {
        databaseRepository.saveNewEntry(groceryItem)
    }

    override fun toggleItemMarked(id: String, isMarked: Boolean) {
        databaseRepository.toggleItemMarked(id, isMarked)
    }

    override fun fetchGroceryData(): Flow<List<GroceryItem>> {
        TODO("Not yet implemented")
    }


}