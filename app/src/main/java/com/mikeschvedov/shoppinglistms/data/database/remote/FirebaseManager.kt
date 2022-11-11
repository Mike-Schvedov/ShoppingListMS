package com.mikeschvedov.shoppinglistms.data.database.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mikeschvedov.shoppinglistms.interfaces.OnDataChangedListener
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine


class FirebaseManager @Inject constructor(
    database: FirebaseDatabase,
    firebaseAuth: FirebaseAuth
) {

    private val databaseReference = database.reference
    private val user = firebaseAuth.currentUser!!

    private val rootName = "allShoppingLists"
    private val shoppinglist = "ShoppingList-001-test"
    //private shoppingListID = getShoppingListID()

    fun addNewEntry(entry: GroceryItem) {
        databaseReference.child(rootName).child(shoppinglist).child(entry.id).setValue(entry)
            .addOnSuccessListener {
                println("New Entry Added")
            }
            .addOnFailureListener {
                println("Failed to Add New Entry")
            }
    }

    fun updateItemIsMarked(item: GroceryItem, isMarked: Boolean) {
        databaseReference.child(rootName).child(shoppinglist).child(item.id).child("marked").setValue(isMarked)
            .addOnSuccessListener {
                println("Data was updated")
            }
            .addOnFailureListener {
                println("Failed to update data")
            }
    }

    fun readAllItemsFromFirebase(callback: OnDataChangedListener) {
        var items: List<GroceryItem>
        databaseReference.child(rootName).child(shoppinglist).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items = dataSnapshot.children.mapNotNull { it.getValue(GroceryItem::class.java) }.toList()
                items.forEach {
                    println("items: ${it.name} | ${it.amount} | ${it.marked}")
                }
                callback.onChange(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("There was some error reading the data")
            }
        })
    }



}
