package com.mikeschvedov.shoppinglistms.data.repository

import com.mikeschvedov.shoppinglistms.models.GroceryItem
import javax.inject.Inject


class RepositoryImpl @Inject constructor() : Repository {

    //    override fun addNote(note: Note) = notesDao.addNote(note)
//
//    override fun getAllNotes() : List<Note> = notesDao.getAllNotes()
//
//    override fun deleteNote(note: Note) = notesDao.deleteNote(note)
    override fun saveNewEntry(groceryItem: GroceryItem) {
        println("this is the item inside the repo: ${groceryItem.name} | ${groceryItem.amount}")
    }

}