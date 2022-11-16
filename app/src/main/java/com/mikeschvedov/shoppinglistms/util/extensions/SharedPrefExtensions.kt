package com.mikeschvedov.shoppinglistms.util

import android.content.Context
import android.content.SharedPreferences

const val MAIN_PREFERENCES = "my_main_pref"
const val LIST_ID = "assigned_list"

fun Context.setCurrentListId(listId: String){
    val sharedPreferences: SharedPreferences =
        getSharedPreferences(MAIN_PREFERENCES, Context.MODE_PRIVATE)
    sharedPreferences.edit().putString(LIST_ID, listId).apply()
}

fun Context.getCurrentListId(): String{
    val sharedPreferences: SharedPreferences =
        getSharedPreferences(MAIN_PREFERENCES, Context.MODE_PRIVATE)
    return  sharedPreferences.getString(LIST_ID, "none") ?: "Error finding Id"
}