package com.mikeschvedov.shoppinglistms.data.repository

import com.mikeschvedov.shoppinglistms.models.GroceryItem

interface Repository  {
    fun saveNewEntry(groceryItem: GroceryItem)

//    fun addNote(note: Note)
//
//    fun getAllNotes() : List<Note>
//
//    fun deleteNote(note: Note)

}