package com.example.etbo5ly.authentication.google_signup

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import com.example.etbo5ly.authentication.signup.SignUpViewModel
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

fun googleSignUp(response: GetCredentialResponse, signUpViewModel: SignUpViewModel) {
    if (response.credential is CustomCredential &&
        response.credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        val tokenId = GoogleIdTokenCredential.createFrom(response.credential.data)
        signUpViewModel.signUpWithGoogle(tokenId.idToken)
    }
}