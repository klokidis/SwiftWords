package com.yukuro.swiftwords.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.yukuro.swiftwords.MainActivity
import com.yukuro.swiftwords.R

fun showNotification(context: Context, title: String, content: String, streak: Int) {
    val channelId = "daily_notification_channel"
    val notificationId = 1001

    // Check if the notification permission is granted (Android 13+)
    if (ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // If permission is not granted, do not show the notification
        return
    }

    // Create a notification channel for Android 8.0 and higher
    val channel = NotificationChannel(
        channelId,
        "Daily Notification",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Channel for daily 7 PM notifications"
    }
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)

    // Create an Intent that opens the app (MainActivity in this case)
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    // Create a PendingIntent with the intent
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Create a LargeIcon (colored) based on the streak level
    val largeIcon = BitmapFactory.decodeResource(
        context.resources,
        when {
            streak < 5 -> R.drawable.fire_ending

            streak < 20 -> R.drawable.fire2_end

            streak < 30 -> R.drawable.fire3_end

            streak < 40 -> R.drawable.fire4_end

            streak < 50 -> R.drawable.fire5_end

            streak >= 60 -> R.drawable.fire6_end

            else -> R.drawable.fire_ending
        }
    )

    val icon = when {
        streak < 5 -> R.drawable.fire_on

        streak < 20 -> R.drawable.fire2

        streak < 30 -> R.drawable.fire3

        streak < 40 -> R.drawable.fire4

        streak < 50 -> R.drawable.fire5

        streak >= 60 -> R.drawable.fire6

        else -> R.drawable.fire_on
    }

    // Create the notification
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(icon)
        .setLargeIcon(largeIcon) // Large colored icon
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent) // Set the intent to open the app when clicked
        .build()

    // Show the notification
    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, notification)
    }
}

