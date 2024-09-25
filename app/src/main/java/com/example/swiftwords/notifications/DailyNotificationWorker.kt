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
        val nickname = inputData.getString("nickname") ?: "User"
        val streak = inputData.getBoolean("streak", false)

        // Customize the notification with the user data
        val content = "Hey $nickname! Your streak is $streak days and your high score is ."

        // Show the notification with the user details
        showNotification(
            applicationContext,
            "Daily Reminder",
            content
        )
        return Result.success()
    }
}
