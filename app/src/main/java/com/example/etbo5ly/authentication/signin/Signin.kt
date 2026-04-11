package com.example.etbo5ly.authentication.signin

import com.example.etbo5ly.authentication.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.authentication.AuthenticationRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Signin() : ViewModel() {
    private val repo: AuthenticationRepo by lazy {AuthenticationRepo()}
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


