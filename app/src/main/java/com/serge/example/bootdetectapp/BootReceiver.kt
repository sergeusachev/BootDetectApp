package com.serge.example.bootdetectapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.util.concurrent.TimeUnit

/**
 * Created by serge on 07.02.18.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("MY_BOOT", "BootReceiver: onReceive")
        /*Toast.makeText(context.applicationContext, intent.action, Toast.LENGTH_LONG).show()
        val startActivityIntent = Intent(context, MainActivity::class.java)
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(startActivityIntent)*/

        setAlarm(context)
    }

    private fun setAlarm(context: Context) {
        Log.i("MY_BOOT", "BootReceiver: setAlarm")
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