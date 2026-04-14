package com.example.etbo5ly.authentication.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.authentication.AuthenticationRepo
import com.example.etbo5ly.authentication.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class changePassword: ViewModel() {

    private val repo = AuthenticationRepo()
    private val _state = MutableStateFlow<State>(State.Idle)
    val status: StateFlow<State> = _state.asStateFlow()

    fun changePassword(code: String?, newPass: String, rePass: String) {
            viewModelScope.launch {
                    if(newPass.equals(rePass)){
                        _state.value = State.Loading
                        try {
                           if (repo.changePassword(code, newPass)) {
                              _state.value = State.Success
                           } else {
                                _state.value = State.Fail("fail")
                           }
                        } catch (e: Exception) {
                            _state.value = State.Fail("Error")
                        }
                    }
            }
    }
}