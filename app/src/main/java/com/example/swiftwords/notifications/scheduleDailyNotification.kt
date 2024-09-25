package com.example.swiftwords.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(context: Context, lastDatePlayed: String, character: Boolean) {
    val currentTime = Calendar.getInstance()

    // Calculate the time 12 hours from now
    val notificationTime = Calendar.getInstance().apply {
        add(Calendar.HOUR_OF_DAY, 12) // Add 12 hours to the current time
    }

    // Ensure the notification time is before 7 PM
    if (notificationTime.get(Calendar.HOUR_OF_DAY) >= 19) {
        // Set to 7 PM if the calculated time is after 7 PM
        notificationTime.set(Calendar.HOUR_OF_DAY, 19)
        notificationTime.set(Calendar.MINUTE, 0)
        notificationTime.set(Calendar.SECOND, 0)
    }

    // Calculate the delay
    var delay = notificationTime.timeInMillis - currentTime.timeInMillis
    if (delay < 0) {
        delay += TimeUnit.DAYS.toMillis(1) // Schedule for the next day
    }

    // Prepare UserDetails data to pass to Worker
    val userData = Data.Builder()
        .putString("nickname", lastDatePlayed)
        .putBoolean("streak", character)
        .build()

    // Create a OneTimeWorkRequest that triggers at the delay
    val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(userData) // Pass the data to Worker
        .build()

    // Enqueue the work request
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "DailyNotification",
        ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
        workRequest
    )
}
