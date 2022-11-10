package com.mikeschvedov.shoppinglistms.interfaces

import com.mikeschvedov.shoppinglistms.models.GroceryItem

 fun interface OnDataChangedListener{
    fun onChange(
        items: List<GroceryItem>
    )
}