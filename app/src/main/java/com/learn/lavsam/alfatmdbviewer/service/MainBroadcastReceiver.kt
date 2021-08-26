package com.learn.lavsam.alfatmdbviewer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MainBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        StringBuilder().apply {
            append("MESSAGE FROM SYSTEM:\n")
            append("Action: ${intent?.action}")
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}