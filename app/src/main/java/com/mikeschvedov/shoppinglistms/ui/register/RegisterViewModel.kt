package com.mikeschvedov.shoppinglistms.ui.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn

class RegisterViewModel: ViewModel() {

    // Input flows
    private val _emailInput = MutableStateFlow("")
    private val _passwordInput = MutableStateFlow("")

    private var _errorMessage = MutableStateFlow("")
    var errorMessage = _errorMessage

    val loginState  =
        combine(_emailInput, _passwordInput) { emailInput, passwordInput ->
            // check if the format is valid
            val isEmailCorrect = emailInput.matches(Patterns.EMAIL_ADDRESS.toRegex())
            val isPasswordCorrect = passwordInput.length > 8
            // set the error message
            _errorMessage.value = when {
                isEmailCorrect.not() -> "Email is not valid"
                isPasswordCorrect.not() -> "Password should be 8 character long"
                else -> "Unknown"
            }
            // only if both are true the loginState will be true
            return@combine isEmailCorrect and isPasswordCorrect
        }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)


    fun setEmailInput(name: String) {
        _emailInput.value = name
    }

    fun setPasswordInput(password: String) {
        _passwordInput.value = password
    }
}