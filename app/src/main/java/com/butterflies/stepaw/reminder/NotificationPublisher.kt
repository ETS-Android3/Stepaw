package com.butterflies.stepaw.reminder

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationPublisher : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        lateinit var reminderLabel:String
        val b = intent.extras
        if (b!!.containsKey("reminderlabel")) {
            Log.d("publisher", b.containsKey("reminderlabel").toString())
            reminderLabel = b.getString("reminderlabel") ?: "Stepaw"
        }
        val notificationHelper = NotificationHelper(context, reminderLabel)
        val nb = notificationHelper.channelNotification
        notificationHelper.manager?.notify(1, nb.build())
        val notification = nb.build()
        notification.defaults = notification.defaults or Notification.DEFAULT_VIBRATE
        notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
    }
}