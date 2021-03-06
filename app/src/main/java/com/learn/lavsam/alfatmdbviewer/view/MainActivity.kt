package com.learn.lavsam.alfatmdbviewer.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.learn.lavsam.alfatmdbviewer.BuildConfig
import com.learn.lavsam.alfatmdbviewer.R
import com.learn.lavsam.alfatmdbviewer.cloudmessage.CHANNEL_ID
import com.learn.lavsam.alfatmdbviewer.databinding.MainActivityBinding
import java.text.SimpleDateFormat
import java.util.*

private const val MY_APPLICATION_ID = BuildConfig.APPLICATION_ID
private const val DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"

class MainActivity : AppCompatActivity() {

    private val tickReceiver by lazy { makeBroadcastReceiver() }

    companion object {
        private fun getCurrentTimeStamp(): String {
            val simpleDateFormat = SimpleDateFormat(DATE_TIME_PATTERN, Locale.US)
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(notificationManager)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    getString(R.string.notificationLogTag),
                    getString(R.string.notificationTokenFail),
                    task.exception
                )
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d(getString(R.string.notificationLogTag), token!!)
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = getString(R.string.notificationChannelName)
        val descriptionText = getString(R.string.notificationDescription)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
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
            val logTag = MY_APPLICATION_ID.substring(MY_APPLICATION_ID.lastIndexOf(".") + 1)
            Log.d(logTag, getString(R.string.log_message_recevied_not_registered), e)
        }
    }

    private fun makeBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action == Intent.ACTION_TIME_TICK) {
                    val currentTime = getCurrentTimeStamp()
                    findViewById<TextView>(R.id.container).showToast(getString(R.string.receivied) + " " + currentTime)
                }
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(tickReceiver)
        super.onDestroy()
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_actions_item_list_movies -> {
                addFragment(MainFragment())
                return true
            }
            R.id.menu_actions_item_list_actors -> {
                addFragment(ActorFragment())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
