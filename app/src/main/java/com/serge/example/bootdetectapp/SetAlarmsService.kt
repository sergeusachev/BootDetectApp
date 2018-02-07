package com.serge.example.bootdetectapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.JobIntentService
import android.util.Log
import java.util.concurrent.TimeUnit

/**
 * Created by serge on 07.02.18.
 */
class SetAlarmsService : JobIntentService() {

    companion object {
        const val KEY_ID_SCHEDULE = "id_schedule"
        const val KEY_SCHEDULING_PERIOD = "scheduling_period"
    }

    override fun onHandleWork(intent: Intent) {
        Log.i("MY_BOOT", "SetAlarmsService: onHandleWork")

        setAlarm(
                applicationContext,
                1313,
                TimeUnit.MINUTES.toMillis(5))
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MY_BOOT", "SetAlarmsService: onDestroy")
    }

    private fun setAlarm(context: Context, idSchedule: Long, periodInMillis: Long) {
        Log.i("MY_BOOT", "SetAlarmsService: setAlarm")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(KEY_ID_SCHEDULE, idSchedule)
        intent.putExtra(KEY_SCHEDULING_PERIOD, periodInMillis)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val triggerTime = System.currentTimeMillis() + periodInMillis

        setAlarmDependingOnAPI(
                alarmManager,
                triggerTime,
                pendingIntent
        )
    }

    private fun setAlarmDependingOnAPI(alarmManager: AlarmManager, triggerTime: Long,
                                       pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            )
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            )
        } else {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            )
        }
    }
}