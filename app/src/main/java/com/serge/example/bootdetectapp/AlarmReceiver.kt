package com.serge.example.bootdetectapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import android.util.Log
import java.util.concurrent.TimeUnit

/**
 * Created by serge on 07.02.18.
 */
class AlarmReceiver : BroadcastReceiver() {

    //intent has KEY_ID_SCHEDULE
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("MY_BOOT", "AlarmReceiver: onReceive!!!!!")

        val scheduleId = intent.getLongExtra(SetAlarmsService.KEY_ID_SCHEDULE, -1)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(
                    "default",
                    "BootAppChannel",
                    NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = "It's BootApp notification channel"
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(notificationChannel)

        }

        val notification = NotificationCompat.Builder(context, "default")
                .setSmallIcon(android.R.drawable.btn_plus)
                .setContentTitle("Notification")
                .setContentText("Alarm id: $scheduleId")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        notificationManager.notify(1, notification)

        //Intent need pass onReceive time
        // for right set alarm time(because JobScheduler not start at once)
        val intentJob = Intent()
        intentJob.putExtra(
                SetAlarmsService.KEY_JOB,
                SetAlarmsService.KEY_JOB_RESTART_ALARM
        )

        JobIntentService.enqueueWork(
                context,
                SetAlarmsService::class.java,
                1,
                intentJob)
    }
}