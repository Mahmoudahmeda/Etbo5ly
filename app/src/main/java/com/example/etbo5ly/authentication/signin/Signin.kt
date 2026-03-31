package com.example.etbo5ly.authentication.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.authentication.AuthenticationRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Signin(
    private val repo: AuthenticationRepo = AuthenticationRepo()
) : ViewModel() {

    val isLoggedIn: Boolean = repo.isUserLoggedIn()
    val isLoggedDestination: String = if (isLoggedIn) "home" else "login"

    private val _state = MutableStateFlow<State>(State.Idle)
    var status: StateFlow<State> = _state.asStateFlow()
    fun LoginWithEmail(Email: String, Pass: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            if (repo.EmailsignIn(Email, Pass)) {
                _state.value = State.Success
            } else {
                _state.value = State.Fail("Login Failed")
            }
        }
    }

    fun LoginWithGoogle(TokenId: String?){
        if(TokenId == null){
            _state.value = State.Fail("Login Failed")
            return
        }
        viewModelScope.launch {
            _state.value = State.Loading
            if (repo.GooglesignIn(TokenId)) {
                _state.value = State.Success
            } else {
                _state.value = State.Fail("Login Failed")
            }
        }
    }

    fun LoginWithFacebook(tokenId: String){
        viewModelScope.launch {
            val success = repo.facebooksingIn(tokenId)

            if (success) {
                _state.value = State.Success
            } else {
                _state.value = State.Fail("Login Failed")
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

sealed class State{
    object Idle: State()
    object Loading: State()
    object Success: State()
    data class Fail(val msg: String): State()
}
