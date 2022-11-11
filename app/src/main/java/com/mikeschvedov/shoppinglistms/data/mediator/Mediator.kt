package com.mikeschvedov.shoppinglistms.data.mediator

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

    override fun toggleItemMarked(item: GroceryItem, isMarked: Boolean) {
        databaseRepository.toggleItemMarked(item, isMarked)
    }

    override suspend fun fetchGroceryData(): Flow<List<GroceryItem>> = flow {
        databaseRepository.fetchGroceryData().collect{
            emit(it)
        }
    }

}