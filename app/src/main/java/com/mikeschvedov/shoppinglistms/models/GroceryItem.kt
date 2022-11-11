package com.mikeschvedov.shoppinglistms.models

import java.util.*

data class GroceryItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val amount: String? = null,
    val marked: Boolean = false
)