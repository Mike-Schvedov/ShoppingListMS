package com.mikeschvedov.shoppinglistms.util

import android.content.Context
import android.content.SharedPreferences

const val MAIN_PREFERENCES = "my_main_pref"
const val LIST_ID = "assigned_list"
const val MESSAGE_LOCK_STATE = "lock_state"

// --- TOPIC --- //

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

// --- MESSAGE LOCK STATE --- //
// A lock to prevent receiving notification when we are the sender.

fun Context.setMessageLockState(isLocked: Boolean){
    val sharedPreferences: SharedPreferences =
        getSharedPreferences(MAIN_PREFERENCES, Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean(MESSAGE_LOCK_STATE, isLocked).apply()
}

fun Context.getMessageLockState(): Boolean{
    val sharedPreferences: SharedPreferences =
        getSharedPreferences(MAIN_PREFERENCES, Context.MODE_PRIVATE)
    return  sharedPreferences.getBoolean(MESSAGE_LOCK_STATE, false)
}