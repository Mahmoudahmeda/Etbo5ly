package com.example.etbo5ly.authentication.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.authentication.AuthenticationRepo
import com.example.etbo5ly.authentication.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val repo: AuthenticationRepo by lazy {AuthenticationRepo()}
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Idle)
    var status: StateFlow<State> = _state.asStateFlow()

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun performSignUp() {
        viewModelScope.launch {
            _state.value = State.Loading
            if (repo.createUser(_username.value, _email.value, _password.value)) {
                _state.value = State.Success
            } else {
                _state.value = State.Fail("Sign Up Failed")
            }
        }
    }
    fun LoginasgGuest(){
        viewModelScope.launch {
            _state.value = State.Loading
            if (repo.guestSignIn()) {
                _state.value = State.Success
            } else {
                _state.value = State.Fail("Login Failed")
            }
        }
    }
}