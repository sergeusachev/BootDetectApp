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
//        const val KEY_SCHEDULING_PERIOD = "scheduling_period"

        const val KEY_JOB = "key_job"
        const val KEY_JOB_SET_ALARM_AFTER_BOOT = 1
        const val KEY_JOB_RESTART_ALARM = 2

    }

    override fun onHandleWork(intent: Intent) {
        Log.i("MY_BOOT", "SetAlarmsService: onHandleWork")

        val kindOfJob = intent.getIntExtra(KEY_JOB, -1)

        when(kindOfJob) {
            KEY_JOB_SET_ALARM_AFTER_BOOT -> setAlarmsAfterReboot()
            KEY_JOB_RESTART_ALARM -> {
                val scheduleId = intent.getLongExtra(KEY_ID_SCHEDULE, -1)//for finding period in DB
                restartAlarm(scheduleId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MY_BOOT", "SetAlarmsService: onDestroy")
    }

    //After reboot
    private fun setAlarmsAfterReboot() {
        Log.i("MY_BOOT", "SetAlarmsService: setAlarmsAfterReboot")
        //get scheduleId, period from DB
        val scheduleId = 1313L
        val startTimeOffset = TimeUnit.SECONDS.toMillis(10)//calculates depends on boot time, first start and period(from DB)

        setAlarm(
                applicationContext,
                scheduleId,
                startTimeOffset
        )
    }

    //From AlarmReceiver
    private fun restartAlarm(scheduleRestartId: Long) {
        Log.i("MY_BOOT", "SetAlarmsService: restartAlarm")
        val periodInMillis = TimeUnit.MINUTES.toMillis(5)//get repeating period from DB by scheduleRestartId
        val realReceiveAlarmTime = 0L //for right set alarm time(because JobScheduler not start at once)
        val rightTimeTrigger = periodInMillis - realReceiveAlarmTime

        setAlarm(
                applicationContext,
                scheduleRestartId,
                rightTimeTrigger
        )

    }

    private fun setAlarm(context: Context, scheduleId: Long, startTimeOffset: Long) {
        Log.i("MY_BOOT", "SetAlarmsService: setAlarm")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(KEY_ID_SCHEDULE, scheduleId)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val triggerTime = System.currentTimeMillis() + startTimeOffset

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