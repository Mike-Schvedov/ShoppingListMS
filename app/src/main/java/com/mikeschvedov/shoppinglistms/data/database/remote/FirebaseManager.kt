package com.mikeschvedov.shoppinglistms.data.database.remote

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mikeschvedov.shoppinglistms.interfaces.OnGroceryItemChangedListener
import com.mikeschvedov.shoppinglistms.interfaces.OnStringChangedListener
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.models.User
import com.mikeschvedov.shoppinglistms.util.getCurrentListId
import javax.inject.Inject


class FirebaseManager @Inject constructor(
    database: FirebaseDatabase,
    firebaseAuth: FirebaseAuth,
     context: Context
) {

    private val databaseReference = database.reference
    private val user = firebaseAuth.currentUser!!

    private val shoppingRoot = "allShoppingLists"
    private val usersRoot = "users"
    private val shoppingListID = context.getCurrentListId()

    // Create new user
    fun createNewUser(user: User) {
        databaseReference.child(usersRoot).child(user.id).setValue(user)
            .addOnSuccessListener {
                println("New User Created")
            }
            .addOnFailureListener {
                println("Failed to Create New User")
            }
    }

    fun getUserConnectedShoppingListID(callback: OnStringChangedListener){
        databaseReference.child(usersRoot).child(user.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val id = dataSnapshot.child("shoppinglist").value.toString()
                callback.onChange(id)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("There was some error reading the data")
            }
        })
    }

    fun addNewEntry(entry: GroceryItem) {
        databaseReference.child(shoppingRoot).child(shoppingListID).child(entry.id).setValue(entry)
            .addOnSuccessListener {
                println("New Entry Added")
            }
            .addOnFailureListener {
                println("Failed to Add New Entry")
            }
    }

    fun updateItemIsMarked(item: GroceryItem, isMarked: Boolean) {
        databaseReference.child(shoppingRoot).child(shoppingListID).child(item.id).child("marked").setValue(isMarked)
            .addOnSuccessListener {
                println("Data was updated")
            }
            .addOnFailureListener {
                println("Failed to update data")
            }
    }

    fun readAllItemsFromFirebase(callback: OnGroceryItemChangedListener) {
        var items: List<GroceryItem>
        databaseReference.child(shoppingRoot).child(shoppingListID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items = dataSnapshot.children.mapNotNull { it.getValue(GroceryItem::class.java) }.toList()
                items.forEach {
                    println("items: ${it.name} | ${it.amount} | ${it.marked}")
                }
                println("FETCHING LIST BASED ON $shoppingListID shoppinglist id")
                callback.onChange(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("There was some error reading the data")
            }
        })
    }



}
