package com.learn.lavsam.alfatmdbviewer.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.learn.lavsam.alfatmdbviewer.R
import com.learn.lavsam.alfatmdbviewer.databinding.MainActivityBinding
import com.learn.lavsam.alfatmdbviewer.service.MainBroadcastReceiver
import java.text.SimpleDateFormat
import java.util.*

private val receiver = MainBroadcastReceiver()

class MainActivity : AppCompatActivity() {

    private val tickReceiver by lazy { makeBroadcastReceiver() }

    companion object {
        private fun getCurrentTimeStamp(): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val now = Date()
            return simpleDateFormat.format(now)
        }
    }

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment.newInstance())
                .commitNow()
        }
        registerReceiver(receiver, IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY))
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(tickReceiver)
        } catch (e: IllegalArgumentException) {
            Log.d("MyBroadcast", "Time tick Receiver not registered", e)
        }
    }

    private fun makeBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action == Intent.ACTION_TIME_TICK) {
                    val currentTime = getCurrentTimeStamp()
                    findViewById<TextView>(R.id.container).showToast("Recivied: " + currentTime)
                }
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}
