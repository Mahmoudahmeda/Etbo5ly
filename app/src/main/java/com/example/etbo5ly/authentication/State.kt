package com.example.etbo5ly.authentication

sealed class State{
    object Idle: State()
    object Loading: State()
    object Success: State()
    data class Fail(val msg: String): State()
}