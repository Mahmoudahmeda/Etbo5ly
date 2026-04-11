package com.example.etbo5ly.authentication.emailVerify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.authentication.AuthenticationRepo
import com.example.etbo5ly.authentication.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class emailVerify: ViewModel() {
    private val repo: AuthenticationRepo by lazy {AuthenticationRepo()}
    private val _state = MutableStateFlow<State>(State.Idle)
    var status: StateFlow<State> = _state.asStateFlow()

    fun sendEmail(email: String){
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                if (repo.sendPasswordRestEmail(email)) {
                    _state.value = State.Success
                } else {
                    _state.value = State.Fail("send failed")
                }
            }catch (e: Exception){
                _state.value = State.Fail("Error")
            }

        }
    }
}
