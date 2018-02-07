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
        /*Toast.makeText(context.applicationContext, intent.action, Toast.LENGTH_LONG).show()
        val startActivityIntent = Intent(context, MainActivity::class.java)
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(startActivityIntent)*/
        JobIntentService.enqueueWork(context, AlarmsJobIntentService::class.java, 1, Intent())
    }



}