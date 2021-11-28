package com.butterflies.stepaw.reminder

import android.content.Intent
import android.content.ContextWrapper
import android.app.NotificationManager
import android.annotation.TargetApi
import android.os.Build
import android.app.NotificationChannel
import com.butterflies.stepaw.reminder.NotificationHelper
import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import com.butterflies.stepaw.R

internal class NotificationHelper(base: Context?, reminderLabel: String) : ContextWrapper(base) {
    private var notificationManager: NotificationManager? = null
    val label=reminderLabel
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            channelID, channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }

    @get:SuppressLint("LogNotTimber")
    val channelNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle("Scheduled Alert")
            .setContentText(label)
            .setSmallIcon(R.drawable.icon_title)

    companion object {
        const val channelID = "channelID"
        const val channelName = "Channel Name"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}