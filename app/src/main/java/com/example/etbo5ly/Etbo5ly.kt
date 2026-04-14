package com.example.etbo5ly

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp

class Etbo5ly: Application() {
    override fun onCreate() {
        super.onCreate()
        try{
            FirebaseApp.initializeApp(this)
        }catch (e: Exception){
            Log.d("Init","FireBaseApp init failed")
        }
    }
}