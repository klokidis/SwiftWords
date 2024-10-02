package com.example.swiftwords.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailyNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Retrieve user data passed from the schedule function
        //val nickname = inputData.getString("nickname") ?: "User"
        val streak = inputData.getInt("streak", 1)

        // Customize the notification with the user data
        val content = "Don't forget Streaky!! "

        // Show the notification with the user details
        showNotification(
            applicationContext,
            "Reminder",
            content,
            streak
        )
        return Result.success()
    }
}
