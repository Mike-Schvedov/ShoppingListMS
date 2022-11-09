package com.mikeschvedov.shoppinglistms.models.state

data class RegisterState(
    var isEmailValid: Boolean,
    var isPasswordValid: Boolean,
    var isConfirmPasswordValid: Boolean
)