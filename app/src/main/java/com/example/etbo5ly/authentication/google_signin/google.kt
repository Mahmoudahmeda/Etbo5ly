package com.example.etbo5ly.authentication.google_signin

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import com.example.etbo5ly.authentication.signin.Signin
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

fun google (response: GetCredentialResponse, signviewModel: Signin){
    if (response.credential is CustomCredential && response.credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        val tokenId = GoogleIdTokenCredential
            .createFrom(response.credential.data)
        signviewModel.LoginWithGoogle(tokenId.idToken)
    }
}