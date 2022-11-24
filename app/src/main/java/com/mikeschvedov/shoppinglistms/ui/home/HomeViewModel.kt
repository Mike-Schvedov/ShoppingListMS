package com.mikeschvedov.shoppinglistms.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.mikeschvedov.shoppinglistms.data.mediator.MediatorProtocol
import com.mikeschvedov.shoppinglistms.data.network.models.PushNotification
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.ui.adapters.GroceryListAdapter
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import com.mikeschvedov.shoppinglistms.util.utility_models.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediator: MediatorProtocol,
    private val currentUser: FirebaseUser?,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val adapter = GroceryListAdapter() { itemClicked ->
        _selectedItem.postValue(SingleEvent(itemClicked))
        // We take the current state and invert it
        val changedState = itemClicked.marked.not()
        toggleItemMarked(itemClicked, changedState)
    }

    val groceryListLiveData: LiveData<List<GroceryItem>> get() = _groceryListLiveData
    private val _groceryListLiveData = MutableLiveData<List<GroceryItem>>()

    private val _selectedItem = MutableLiveData<SingleEvent<GroceryItem>>()
    val selectedItem: LiveData<SingleEvent<GroceryItem>> get() = _selectedItem

    val shoppingListID: LiveData<String> get() = _shoppingListID
    private val _shoppingListID = MutableLiveData<String>()

    // ----- REMOTE DATABASE ----- //
    fun saveNewEntry(groceryItem: GroceryItem) {
        viewModelScope.launch {
            mediator.saveNewEntry(groceryItem)
        }
    }

    fun fetchGroceryData() {
        LoggerService.info("fetchGroceryData() - HomeViewModel")
        viewModelScope.launch {
            mediator.fetchGroceryData()
                .collect {
                    _groceryListLiveData.postValue(it)
                }
        }
    }

    fun getUserConnectedShoppingListID() {
        println("getUserConnectedShoppingListID() - HomeViewModel")
        viewModelScope.launch(Dispatchers.IO) {
            mediator.getUserConnectedShoppingListID()
                .collect {
                    println("Returning List ID in HomeViewModel")
                    _shoppingListID.postValue(it)
                }
        }
    }

    private fun toggleItemMarked(item: GroceryItem, isMarked: Boolean) {
        viewModelScope.launch {
            mediator.toggleItemMarked(item, isMarked)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            mediator.deleteAll()
        }
    }

    fun deleteMarkedItems() {
        viewModelScope.launch {
            mediator.deleteMarkedItems()
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return currentUser
    }

    fun getAdapter(): GroceryListAdapter {
        return adapter
    }

    fun signOutUser() {
        auth.signOut()
    }

    fun subscribeDeviceToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("mike")
            .addOnCompleteListener {
            if (it.isSuccessful) {
                LoggerService.info("successfully subscribed to the topic")
            } else {
                LoggerService.info("failed to subscribe to the topic")
            }
        }
            .addOnFailureListener {
                LoggerService.info("failed to subscribe to the topic : ${it.message}")
            }
    }

    fun sendNotification(it: PushNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            mediator.sendNotification(it)
        }
    }

}