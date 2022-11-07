package com.mikeschvedov.shoppinglistms.ui.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikeschvedov.shoppinglistms.models.state.LoginState
import kotlinx.coroutines.flow.*

class LoginViewModel : ViewModel() {

    // Input flows
    private val _emailInput = MutableStateFlow("")
    private val _passwordInput = MutableStateFlow("")

    private var _errorMessage = MutableStateFlow("")
    var errorMessage = _errorMessage


    //TODO: ASK - should we leave combine and add seperate boolean for each field?
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