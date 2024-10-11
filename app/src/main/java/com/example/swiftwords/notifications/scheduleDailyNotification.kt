package com.example.swiftwords.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(context: Context, streakLevel: Int) {
    // Check if the notification permission is granted (Android 13+)
    if (ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // If permission is not granted, do not show the notification
        return
    }

    // Get the current time
    val currentTime = Calendar.getInstance()
    // get the time
    val notificationTime = Calendar.getInstance()

    // Check if the current time is after 2 PM
    if (currentTime.get(Calendar.HOUR_OF_DAY) > 14 ||
        (currentTime.get(Calendar.HOUR_OF_DAY) == 14 && currentTime.get(Calendar.MINUTE) > 0)
    ) {
        // Set the notification time to 2 PM the next day
        notificationTime.add(Calendar.DAY_OF_YEAR, 1)
        notificationTime.set(Calendar.HOUR_OF_DAY, 14)
        notificationTime.set(Calendar.MINUTE, 0)
        notificationTime.set(Calendar.SECOND, 0)
    } else {
        // Set the notification time to 24 hours from now
        notificationTime.add(Calendar.DAY_OF_YEAR, 1)
    }

    // Calculate the delay
    var delay = notificationTime.timeInMillis - currentTime.timeInMillis

    // If the delay is negative, so schedule it for 2 PM the next day?
    if (delay < 0) {
        notificationTime.add(Calendar.DAY_OF_YEAR, 1) // Schedule for tomorrow 2 PM
        delay = notificationTime.timeInMillis - currentTime.timeInMillis
    }

    // Prepare UserDetails data to pass to Worker
    val userData = Data.Builder()
        .putInt("streak", streakLevel)
        .build()

    // Create a OneTimeWorkRequest to trigger the notification after the delay
    val workRequest = OneTimeWorkRequestBuilder<DailyNotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(userData) // Pass data to Worker
        .build()

    // Enqueue the work request using REPLACE policy
    WorkManager.getInstance(context).enqueueUniqueWork(
        "OneTimeDailyNotification", // Unique work name
        ExistingWorkPolicy.REPLACE, // Cancel any existing and requeue
        workRequest
    )
}