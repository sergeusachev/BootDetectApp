package com.serge.example.bootdetectapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.JobIntentService
import android.util.Log

/**
 * Created by serge on 08.02.18.
 */
class RestartAlarmsService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        Log.i("MY_BOOT", "RestartAlarmService: setAlarm")

        val scheduleId = intent.getLongExtra(SetAlarmsService.KEY_ID_SCHEDULE, -1)
        val schedulePeriod = intent.getLongExtra(SetAlarmsService.KEY_SCHEDULING_PERIOD, -1)

        //check in Db if repeating turn off

        if (scheduleId == 1313L) {
            Log.i("MY_BOOT", "RestartAlarmService: ID_OK")
            setAlarm(
                    applicationContext,
                    scheduleId,
                    schedulePeriod
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MY_BOOT", "RestartAlarmsService: onDestroy")
    }

    private fun setAlarm(context: Context, idSchedule: Long, periodInMillis: Long) {
        Log.i("MY_BOOT", "RestartAlarmService: setAlarm")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(SetAlarmsService.KEY_ID_SCHEDULE, idSchedule)
        intent.putExtra(SetAlarmsService.KEY_SCHEDULING_PERIOD, periodInMillis)

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