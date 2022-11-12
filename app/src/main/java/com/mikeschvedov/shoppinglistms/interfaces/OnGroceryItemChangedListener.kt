package com.mikeschvedov.shoppinglistms.interfaces

import com.mikeschvedov.shoppinglistms.models.GroceryItem

 fun interface OnGroceryItemChangedListener{
    fun onChange(
        items: List<GroceryItem>
    )
}