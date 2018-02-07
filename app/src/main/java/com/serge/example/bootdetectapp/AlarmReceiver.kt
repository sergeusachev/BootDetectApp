package com.serge.example.bootdetectapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.util.Log

/**
 * Created by serge on 07.02.18.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("MY_BOOT", "AlarmReceiver: onReceive!!!!!!!!!!!!!!!!!!!!!!!!")
        //Intent need pass onReceive time
        // for right set alarm time(because JobScheduler not start at once)
        JobIntentService.enqueueWork(context, RestartAlarmsService::class.java, 1, intent)
    }
}