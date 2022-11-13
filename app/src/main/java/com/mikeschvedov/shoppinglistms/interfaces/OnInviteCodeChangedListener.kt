package com.mikeschvedov.shoppinglistms.interfaces

import com.mikeschvedov.shoppinglistms.models.GroceryItem

 fun interface OnInviteCodeChangedListener{
    fun onChange(
        list: List<String>
    )
}