package com.example.etbo5ly.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class MealNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("Etbo5lyWorker", "Worker started for: ${inputData.getString("RECIPE_NAME")}")
        // 1. Get the data we passed from the ViewModel
        val recipeName = inputData.getString("RECIPE_NAME") ?: "Meal Time!"

        // 2. Show the notification
        showNotification(recipeName)

        return Result.success()
    }

    private fun showNotification(recipeName: String) {
        val channelId = "meal_reminders"
        val notificationId = System.currentTimeMillis().toInt() // Unique ID

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 3. Create Notification Channel (Required for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Meal Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 4. Build the notification
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Time to cook!")
            .setContentText("It's time to prepare: $recipeName")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // Replace with your app icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}