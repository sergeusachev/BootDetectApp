package com.serge.example.bootdetectapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.concurrent.TimeUnit

/**
 * Created by serge on 07.02.18.
 */
class BootReceiver : BroadcastReceiver() {

    companion object {
        const val PERIOD_IN_MILLIS_TAG = "period_in_millis"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("MY_BOOT", "BootReceiver: onReceive with action: ${intent.action}" )

        setAlarm(context)
    }

    private fun setAlarm(context: Context) {
        Log.i("MY_BOOT", "BootReceiver: setAlarm")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(PERIOD_IN_MILLIS_TAG, TimeUnit.MINUTES.toMillis(5))

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val triggerTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)

        if (Build.VERSION.SDK_INT >= 23) {
            Log.i("MY_BOOT", "Alarm set for api >= 23")
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            )
        } else if (Build.VERSION.SDK_INT >= 19) {
            Log.i("MY_BOOT", "Alarm set for api >= 19")
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            )
        } else {
            Log.i("MY_BOOT", "Alarm set for api < 19")
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            )
        }
    }

}