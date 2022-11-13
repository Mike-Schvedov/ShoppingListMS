package com.mikeschvedov.shoppinglistms.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mikeschvedov.shoppinglistms.data.mediator.Mediator
import com.mikeschvedov.shoppinglistms.models.GroceryItem
import com.mikeschvedov.shoppinglistms.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val mediator: Mediator
): ViewModel() {

    // Input flows
    private val _emailInput = MutableStateFlow("")
    private val _passwordInput = MutableStateFlow("")
    private val _confirmPasswordInput = MutableStateFlow("")

    private var _errorMessage = MutableStateFlow("")
    var errorMessage = _errorMessage

    val inviteCodesList: LiveData<List<String>> get()= _inviteCodesList
    private val _inviteCodesList = MutableLiveData<List<String>>()

    val loginState  =
        combine(_emailInput, _passwordInput, _confirmPasswordInput) { emailInput, passwordInput, confirmPasswordInput ->
            // check if the format is valid
            val isEmailCorrect = emailInput.matches(Patterns.EMAIL_ADDRESS.toRegex())
            val isPasswordCorrect = passwordInput.length > 8
            val isPasswordConfirmed = (confirmPasswordInput == passwordInput)
            // set the error message
            _errorMessage.value = when {
                isEmailCorrect.not() -> "Email is not valid"
                isPasswordCorrect.not() -> "Password should be 8 character long"
                isPasswordConfirmed.not() -> "Both Passwords not matching"
                else -> "Unknown"
            }
            // only if both are true the loginState will be true
            return@combine isEmailCorrect and isPasswordCorrect and isPasswordConfirmed
        }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)


    fun setEmailInput(name: String) {
        _emailInput.value = name
    }

    fun setPasswordInput(password: String) {
        _passwordInput.value = password
    }

    fun setConfirmPasswordInput(confirm: String) {
        _confirmPasswordInput.value = confirm
    }

    fun getAuthentication(): FirebaseAuth {
        return firebaseAuth
    }

    fun addUserToDatabase(user: User) {
        viewModelScope.launch {
            mediator.addUserToDatabase(user)
        }
    }

    fun getAllValidInviteCodes(){
        viewModelScope.launch {
            mediator.getAllValidInviteCodes().collect{
                _inviteCodesList.postValue(it)
            }
        }
    }
}