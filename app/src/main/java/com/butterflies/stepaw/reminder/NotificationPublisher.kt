package com.butterflies.stepaw.reminder

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationPublisher : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val b = intent.extras
        if (b!!.containsKey("reminderlabel")) {
            val l=b.getString("reminderlabel","Stepaw")
            Log.d("reminder", l)
            val notificationHelper = NotificationHelper(context, l)
            val nb = notificationHelper.channelNotification
            notificationHelper.manager?.notify(1, nb.build())
            val notification = nb.build()
            notification.defaults = notification.defaults or Notification.DEFAULT_VIBRATE
            notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
        }
    }
}