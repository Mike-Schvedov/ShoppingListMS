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

    private val adapter = GroceryListAdapter() { shipCallback ->
        _selectedShip.postValue(SingleEvent(shipCallback))
    }

    val groceryListLiveData: LiveData<List<GroceryItem>> get()= _groceryListLiveData
    private val _groceryListLiveData = MutableLiveData<List<GroceryItem>>()

    private val _selectedShip = MutableLiveData<SingleEvent<GroceryItem>>()
    val selectedShip: LiveData<SingleEvent<GroceryItem>> get() = _selectedShip


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
                    print("viewmodel should get this: ${it.toString()}")
                    _groceryListLiveData.postValue(it)
                }
        }
    }

    fun toggleItemMarked(id: String, isMarked: Boolean){
        viewModelScope.launch {
            mediator.toggleItemMarked(id, isMarked)
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