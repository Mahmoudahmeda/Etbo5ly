package com.example.etbo5ly.authentication

import android.util.Log
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.actionCodeSettings
import kotlinx.coroutines.tasks.await

class AuthenticationRepo() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val actionCodeSettings = actionCodeSettings {
        url = "https://etbo5ly-6a4ca303.firebaseapp.com"
        handleCodeInApp = true
        setAndroidPackageName(
            "com.example.etbo5ly",
            true,
            "24"
        )
    }

    fun isLoggedIn(): Boolean {
        return try {
            auth.currentUser != null
        } catch (e: Exception) {
            false
        }
    }

    fun getCurrentUserUid(): String = auth.currentUser?.uid ?: "guest"

    fun getCurrentUserEmail(): String {
        val user = auth.currentUser
        // Try the main email first, then look through provider data (Google/Facebook)
        val email = user?.email?.takeIf { it.isNotBlank() } 
            ?: user?.providerData?.firstOrNull { !it.email.isNullOrBlank() }?.email
            
        return if (email.isNullOrBlank()) "No Email" else email!!
    }

    fun getCurrentUserName(): String {
        val user = auth.currentUser
        val name = user?.displayName?.takeIf { it.isNotBlank() }
            ?: user?.providerData?.firstOrNull { !it.displayName.isNullOrBlank() }?.displayName
            
        return if (name.isNullOrBlank()) "Guest" else name!!
    }

    fun getCurrentUserPhotoUrl(): String? {
        val user = auth.currentUser
        return user?.photoUrl?.toString() 
            ?: user?.providerData?.firstOrNull { it.photoUrl != null }?.photoUrl?.toString()
    }

    fun setCurrentUserPhotoUrl(url: String) {}

    fun signOut() {
        auth.signOut()
    }

    val Username: String? = auth.currentUser?.displayName
    suspend fun EmailsignIn(email: String, pass: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            true
        } catch (e: Exception) {
            Log.e("AuthRepo", "Login Error: ${e.message}", e)
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
        return try {
            val credential = FacebookAuthProvider.getCredential(tokenId)
            auth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun guestSignIn(): Boolean {
        return try {
            auth.signInAnonymously().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun sendPasswordRestEmail(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email, actionCodeSettings).await()
            true
        } catch (e: Exception) {
            Log.d("Fail",e.printStackTrace().toString())
            false
        }
    }

    suspend fun changePassword(code: String?, newPass: String): Boolean {
        return try {
            if (code != null) {
                auth.confirmPasswordReset(code, newPass).await()
                true
            }else{
                false
            }
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun createUser(username: String, email: String, pass: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
                displayName = username
            }
            result.user?.updateProfile(profileUpdates)?.await()
            true
        } catch (e: Exception) {
            Log.e("AuthRepo", "SignUp failed: ${e.message}")
            false
        }
    }
}
