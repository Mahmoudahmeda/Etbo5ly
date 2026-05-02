package com.example.etbo5ly.authentication.facebook_signup

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class facebookSignUp(val token: (String) -> Unit) {
    val callbackManager = CallbackManager.Factory.create()

    fun callback(): FacebookCallback<LoginResult> {
        return object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                token("")
            }
            override fun onError(error: FacebookException) {
                token("")
            }
            override fun onSuccess(result: LoginResult) {
                token(result.accessToken.token)
            }
        }
    }

    fun registercallback() {
        LoginManager.getInstance().registerCallback(callbackManager, callback())
    }

    fun unregistercallback() {
        LoginManager.getInstance().unregisterCallback(callbackManager)
    }
}