package com.example.etbo5ly.authentication

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthenticationRepo{
    private val auth = FirebaseAuth.getInstance()

    fun isUserLoggedIn(): Boolean{
        return auth.currentUser != null
    }

    suspend fun EmailsignIn(email:String, pass:String): Boolean{
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            true
        }catch (e: Exception){
            false
        }
    }

    suspend fun GooglesignIn(TokenId: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(TokenId, null)
            auth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun facebooksingIn(tokenId: String): Boolean {
        return try{
            val credential = FacebookAuthProvider.getCredential(tokenId)
            auth.signInWithCredential(credential).await()
            true
        }catch (e: Exception) {
            false
        }
    }

    suspend fun guestSignIn(): Boolean {
        return try {
            auth.signInAnonymously().await()
            true
        }catch (e: Exception){
            false
        }
    }
}