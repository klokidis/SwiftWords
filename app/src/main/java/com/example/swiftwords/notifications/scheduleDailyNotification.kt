package com.example.swiftwords.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.swiftwords.data.UserDetails
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleDailyNotification(context: Context, userDetails: UserDetails) {
    val currentTime = Calendar.getInstance()

    // Set the time to 7 PM
    val notificationTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 16) // 7 PM
        set(Calendar.MINUTE, 17)
        set(Calendar.SECOND, 0)
    }

    // Calculate the delay
    var delay = notificationTime.timeInMillis - currentTime.timeInMillis
    if (delay < 0) {
        delay += TimeUnit.DAYS.toMillis(1) // Schedule for the next day
    }

    // Prepare UserDetails data to pass to Worker
    val userData = Data.Builder()
        .putString("nickname", userDetails.nickname)
        .putInt("streak", userDetails.streak)
        .putInt("highScore", userDetails.highScore)
        .build()

    // Create a OneTimeWorkRequest that triggers at the delay
    val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(userData) // Pass the data to Worker
        .build()

    // Enqueue the work request (this was missing)
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "DailyNotification",
        ExistingPeriodicWorkPolicy.REPLACE,
        workRequest
    )
}
