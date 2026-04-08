package com.example.etbo5ly.authentication_package

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()


    val authStateListener = MutableLiveData<authstate>()


    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser != null) {
            authStateListener.postValue(authstate.authenticated)
        } else {
            authStateListener.postValue(authstate.unauthenticated)
        }
    }

    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {

            authStateListener.value = authstate.error("Email and password cannot be empty")
            return
        }


        authStateListener.value = authstate.loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    authStateListener.postValue(authstate.authenticated)
                } else {

                    authStateListener.postValue(
                        authstate.error(it.exception?.message ?: "something went wrong")
                    )
                }
            }

    }
}


sealed class authstate {
    object authenticated : authstate()
    object unauthenticated : authstate()
    object loading : authstate()
    data class error(val message: String) : authstate()
}