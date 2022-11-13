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
import javax.inject.Inject


class FirebaseManager @Inject constructor(
    private  val database: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
     private val context: Context
) {

    private val databaseReference = database.reference
    private val currentUserUID = getCurrentUserUID()

    private fun getCurrentUserUID(): String {
        val user = firebaseAuth.currentUser
        val uid = user?.uid
        return uid ?: ""
    }

    private val shoppingRoot = "allShoppingLists"
    private val usersRoot = "users"

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
        databaseReference.child(usersRoot).child(currentUserUID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val id = dataSnapshot.child("shoppinglistid").value.toString()
                println("RETURNING THIS ID FROM FIREBASE: $id")
                callback.onChange(id)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("There was some error reading the data")
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
                println("There was some error reading the data")
            }
        })
    }


    fun addNewEntry(entry: GroceryItem) {
        databaseReference.child(shoppingRoot).child(context.getCurrentListId()).child(entry.id).setValue(entry)
            .addOnSuccessListener {
                println("New Entry Added")
            }
            .addOnFailureListener {
                println("Failed to Add New Entry")
            }
    }

    fun updateItemIsMarked(item: GroceryItem, isMarked: Boolean) {
        databaseReference.child(shoppingRoot).child(context.getCurrentListId()).child(item.id).child("marked").setValue(isMarked)
            .addOnSuccessListener {
                println("Data was updated")
            }
            .addOnFailureListener {
                println("Failed to update data")
            }
    }

    fun readAllItemsFromFirebase(callback: OnGroceryItemChangedListener) {
        var items: List<GroceryItem>
        databaseReference.child(shoppingRoot).child(context.getCurrentListId()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items = dataSnapshot.children.mapNotNull { it.getValue(GroceryItem::class.java) }.toList()
                items.forEach {
                    println("items: ${it.name} | ${it.amount} | ${it.marked}")
                }
                println("FETCHING LIST BASED ON ${context.getCurrentListId()} shoppinglist id")
                callback.onChange(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("There was some error reading the data")
            }
        })
    }



}
