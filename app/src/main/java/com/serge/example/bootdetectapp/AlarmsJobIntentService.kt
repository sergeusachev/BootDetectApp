package com.serge.example.bootdetectapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.util.Log
import java.util.concurrent.TimeUnit

/**
 * Created by serge on 07.02.18.
 */
class AlarmsJobIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        Log.i("MY_BOOT", "AlarmsJobIntentService: onHandleWork")
        setAlarm(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MY_BOOT", "AlarmsJobIntentService: onDestroy")
    }

    private fun setAlarm(context: Context) {
        Log.i("MY_BOOT", "AlarmsJobIntentService: setAlarm")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val triggerTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(20)

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                TimeUnit.MINUTES.toMillis(10),
                pendingIntent
        )
    }
}