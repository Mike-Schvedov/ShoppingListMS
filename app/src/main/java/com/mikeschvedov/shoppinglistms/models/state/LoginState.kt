package com.mikeschvedov.shoppinglistms.models.state

import kotlinx.coroutines.flow.MutableStateFlow

data class LoginState(
    var isEmailValid: Boolean,
    var isPasswordValid: Boolean

)