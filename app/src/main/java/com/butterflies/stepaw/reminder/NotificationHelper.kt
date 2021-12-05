package com.butterflies.stepaw.reminder

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.butterflies.stepaw.R
import android.content.Intent

import android.app.PendingIntent
import android.app.PendingIntent.*
import com.butterflies.stepaw.ChartReport









internal class NotificationHelper(base: Context?, reminderLabel: String) : ContextWrapper(base) {
    private var notificationManager: NotificationManager? = null
    private val messageLabel=reminderLabel
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        Log.d("notificationhelper", messageLabel.length.toString())
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

    var notificationIntent = Intent(this, ChartReport::class.java)
    var conPendingIntent = getActivity(this, 0, notificationIntent, FLAG_UPDATE_CURRENT)
    @get:SuppressLint("LogNotTimber")
    val channelNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle("Stepaw Alert")
            .setContentText(messageLabel)
            .setSmallIcon(R.drawable.icon_title).setContentIntent(conPendingIntent)



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