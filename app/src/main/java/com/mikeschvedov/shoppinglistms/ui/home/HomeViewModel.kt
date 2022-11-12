package com.mikeschvedov.shoppinglistms.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.mikeschvedov.shoppinglistms.data.mediator.MediatorProtocol
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.ui.adapters.GroceryListAdapter
import com.mikeschvedov.shoppinglistms.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediator: MediatorProtocol,
    private val currentUser: FirebaseUser?
    ): ViewModel() {

    private val adapter = GroceryListAdapter() { itemClicked ->
        _selectedItem.postValue(SingleEvent(itemClicked))
        // We take the current state and invert it
        val changedState = itemClicked.marked.not()
        toggleItemMarked(itemClicked, changedState)
    }

    val groceryListLiveData: LiveData<List<GroceryItem>> get()= _groceryListLiveData
    private val _groceryListLiveData = MutableLiveData<List<GroceryItem>>()

    private val _selectedItem = MutableLiveData<SingleEvent<GroceryItem>>()
    val selectedItem: LiveData<SingleEvent<GroceryItem>> get() = _selectedItem

    val shoppingListID: LiveData<String> get()= _shoppingListID
    private val _shoppingListID = MutableLiveData<String>()

    // ----- REMOTE DATABASE ----- //
    fun saveNewEntry(groceryItem: GroceryItem) {
        viewModelScope.launch {
            mediator.saveNewEntry(groceryItem)
        }
    }

    fun fetchGroceryData(){
        viewModelScope.launch {
            mediator.fetchGroceryData()
                .collect {
                    _groceryListLiveData.postValue(it)
                }
        }
    }

    fun getUserConnectedShoppingListID(){
        viewModelScope.launch {
            mediator.getUserConnectedShoppingListID()
                .collect {
                    _shoppingListID.postValue(it)
                }
        }
    }

    private fun toggleItemMarked(item: GroceryItem, isMarked: Boolean){
        viewModelScope.launch {
            mediator.toggleItemMarked(item, isMarked)
        }
    }

    fun deleteAll() {
        TODO("Not yet implemented")
    }

    fun deleteMarkedItems() {
        TODO("Not yet implemented")
    }

    fun getCurrentUser(): FirebaseUser? {
       return currentUser
    }

    fun getAdapter(): GroceryListAdapter {
        return adapter
    }


}