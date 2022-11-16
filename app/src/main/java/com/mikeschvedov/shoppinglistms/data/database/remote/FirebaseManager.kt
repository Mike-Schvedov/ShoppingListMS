package com.mikeschvedov.shoppinglistms.data.database.remote

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mikeschvedov.shoppinglistms.interfaces.OnInviteCodeChangedListener
import com.mikeschvedov.shoppinglistms.interfaces.OnGroceryItemChangedListener
import com.mikeschvedov.shoppinglistms.interfaces.OnStringChangedListener
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.models.User
import com.mikeschvedov.shoppinglistms.util.getCurrentListId
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import javax.inject.Inject

class FirebaseManager @Inject constructor(
    private  val database: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
     private val context: Context
) {

    private val databaseReference = database.reference
    private val currentListId get() = context.getCurrentListId()

    private val shoppingRoot = "allShoppingLists"
    private val usersRoot = "users"

    var clicked: Boolean = false

    private fun getCurrentUserUID(): String {
        val user = firebaseAuth.currentUser
        val uid = user?.uid
        return uid ?: ""
    }

    // Create new user
    fun createNewUser(user: User) {
        databaseReference.child(usersRoot).child(user.id).setValue(user)
            .addOnSuccessListener {
                LoggerService.info("New User Created")
            }
            .addOnFailureListener {
                LoggerService.error("\uD83D\uDCD5Failed to Create New User")
            }
    }

    fun getUserConnectedShoppingListID(callback: OnStringChangedListener){
        println("getUserConnectedShoppingListID() - Firebase")
        val currentUserID = getCurrentUserUID() //This has to be here!! otherwise it gets the wrong id
        LoggerService.debug(" user id = $currentUserID")
        databaseReference.child(usersRoot).child(currentUserID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val id = dataSnapshot.child("shoppinglistid").value.toString()
                LoggerService.debug("\uD83D\uDCD7RETURNING THIS ID FROM FIREBASE: $id based on this user: ${getCurrentUserUID()}")
                callback.onChange(id)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                LoggerService.error("\uD83D\uDCD5There was some error reading the data")
            }
        })
    }

    fun getAllValidInviteCodes(callback: OnInviteCodeChangedListener){
        databaseReference.child(usersRoot).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val id = dataSnapshot.child("shoppinglistid").value.toString()
             //TODO simply get the ids and chop their first 7 characters
                callback.onChange(listOf("code1", "code2", "code3"))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                LoggerService.error("There was some error reading the data")
            }
        })
    }


    fun addNewEntry(entry: GroceryItem) {
        databaseReference.child(shoppingRoot).child(currentListId).child(entry.id).setValue(entry)
            .addOnSuccessListener {
                LoggerService.info("New Entry Added")
            }
            .addOnFailureListener {
                LoggerService.error("Failed to Add New Entry")
            }
    }

    fun updateItemIsMarked(item: GroceryItem, isMarked: Boolean) {
        databaseReference.child(shoppingRoot).child(currentListId).child(item.id).child("marked").setValue(isMarked)
            .addOnSuccessListener {
                LoggerService.info("Data was updated")
            }
            .addOnFailureListener {
                LoggerService.error("Failed to update data")
            }
    }

    fun readAllItemsFromFirebase(callback: OnGroceryItemChangedListener) {
        LoggerService.info("fetchGroceryData() - Firebase")
        var items: List<GroceryItem>
        databaseReference.child(shoppingRoot).child(currentListId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items = dataSnapshot.children.mapNotNull { it.getValue(GroceryItem::class.java) }.toList()
                LoggerService.debug("fetchGroceryData() - FETCHING LIST BASED ON ${currentListId} shoppinglist id")
                callback.onChange(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                LoggerService.error("There was some error reading the data")
            }
        })
    }

    fun deleteAll(){
        databaseReference.child(shoppingRoot).child(currentListId).removeValue()
    }

    fun deleteMarkedItems(){
        // We need this boolean so the onDataChange will not delete entries by itself
        clicked = true

        val currentListReference = databaseReference.child(shoppingRoot).child(currentListId)
        currentListReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && clicked) {
                    dataSnapshot.children.forEach {
                        val itemsKey = it.key                                                        // For each child of our reference(root database) we get the item's (aka "it") key.
                        val itemsBought: Boolean = it.child("marked").value as Boolean          // We get the value of "bought", a child of the item ("it").

                        if (itemsBought) {                                                           // If the current "bought" value is true (checked) then remove the item (according to the key - "itemsKey")
                            if (itemsKey != null) {
                                currentListReference.child(itemsKey).removeValue()
                            }
                        }
                    }
                    // Setting back to false, otherwise it will automatically delete entries
                    clicked = false
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}
