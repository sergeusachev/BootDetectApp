package com.serge.example.bootdetectapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.util.Log

/**
 * Created by serge on 07.02.18.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("MY_BOOT", "BootReceiver: onReceive")

        JobIntentService.enqueueWork(
                context,
                SetAlarmsService::class.java,
                1,
                Intent()
        )
    }



}