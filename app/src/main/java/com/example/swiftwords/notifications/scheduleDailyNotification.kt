package com.example.swiftwords.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(context: Context, lastDatePlayed: String, character: Boolean) {
    // Get the current time
    val currentTime = Calendar.getInstance()


    // Set up the target notification time 24 hours from now
    val notificationTime = Calendar.getInstance().apply {
        add(Calendar.HOUR_OF_DAY, 24)
    }

    // Ensure that the notification time is before or at 7 PM
    if (notificationTime.get(Calendar.HOUR_OF_DAY) > 19 ||
        (notificationTime.get(Calendar.HOUR_OF_DAY) == 19 && notificationTime.get(Calendar.MINUTE) > 0)
    ) {
        // If the notification time is after 7 PM, set it to 7 PM today
        notificationTime.set(Calendar.HOUR_OF_DAY, 19)
        notificationTime.set(Calendar.MINUTE, 0)
        notificationTime.set(Calendar.SECOND, 0)
    }

    // Calculate the delay
    var delay = notificationTime.timeInMillis - currentTime.timeInMillis

    // If the delay is negative, it means we passed 7 PM, so schedule it for 7 PM the next day
    if (delay < 0) {
        notificationTime.add(Calendar.DAY_OF_YEAR, 1) // Schedule for tomorrow 7 PM
        delay = notificationTime.timeInMillis - currentTime.timeInMillis
    }

    // Prepare UserDetails data to pass to Worker
    val userData = Data.Builder()
        .putString("nickname", lastDatePlayed)
        .putBoolean("streak", character)
        .build()

    // Create a OneTimeWorkRequest to trigger the notification after the delay
    val workRequest = OneTimeWorkRequestBuilder<DailyNotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS) // Delay until 7 PM
        .setInputData(userData) // Pass data to Worker
        .build()

    // Enqueue the work request using REPLACE policy
    WorkManager.getInstance(context).enqueueUniqueWork(
        "OneTimeDailyNotification", // Unique work name
        ExistingWorkPolicy.REPLACE, // Cancel any existing and requeue
        workRequest
    )
}