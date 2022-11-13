package com.mikeschvedov.shoppinglistms.data.mediator

import com.mikeschvedov.shoppinglistms.data.repository.RepositoryProtocol
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Mediator @Inject constructor(
    private val databaseRepository: RepositoryProtocol
): MediatorProtocol {

    // ---------------------- Shopping Lists ---------------------- //
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

    // ---------------------- Users ---------------------- //
     override fun addUserToDatabase(user: User){
        databaseRepository.addUserToDatabase(user)
    }

    override suspend fun getUserConnectedShoppingListID(): Flow<String> = flow{
        databaseRepository.getUserConnectedShoppingListID().collect{
            emit(it)
        }
    }

    override suspend fun  getAllValidInviteCodes(): Flow<List<String>> = flow {
        println("Entered the mediator")
        databaseRepository.getAllValidInviteCodes().collect{
            println("Returning from the mediator : $it")
            emit(it)
        }
    }
}