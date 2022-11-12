package com.mikeschvedov.shoppinglistms.interfaces

import com.mikeschvedov.shoppinglistms.models.GroceryItem

 fun interface OnStringChangedListener{
    fun onChange(
        id: String
    )
}