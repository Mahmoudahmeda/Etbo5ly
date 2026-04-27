package com.example.etbo5ly.authentication

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.actionCodeSettings
import kotlinx.coroutines.tasks.await
import kotlin.Boolean

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

    val Username: String? = auth.currentUser?.displayName
    suspend fun EmailsignIn(email: String, pass: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun GooglesignIn(TokenId: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(TokenId, null)
            auth.signInWithCredential(credential).await()
            Log.d("Google","Auth ${TokenId}")
            true
        } catch (e: Exception) {
            Log.d("Google",e.printStackTrace().toString())
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
    suspend fun createUser(username:String ,email: String, pass: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
            true
        }catch (e: Exception){
            false
        }
    }
    fun logout(): Boolean {
        return try {
            auth.signOut()
            true
        }catch (e: Exception){
            false
        }
    }
}