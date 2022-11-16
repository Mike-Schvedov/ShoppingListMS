package com.mikeschvedov.shoppinglistms.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.mikeschvedov.shoppinglistms.R


fun Context.setCardFocus(card: MaterialCardView, hasFocus: Boolean){

    val standardBackground = ContextCompat.getColor(this, R.color.black)
    val focusedBackground = ContextCompat.getColor(this, R.color.orange_medium)

    card.outlineSpotShadowColor = if (hasFocus) focusedBackground else standardBackground
    card.strokeWidth = if (hasFocus) 3 else 0
    card.strokeColor = if (hasFocus) focusedBackground else standardBackground
}
