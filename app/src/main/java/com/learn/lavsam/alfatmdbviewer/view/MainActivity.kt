package com.learn.lavsam.alfatmdbviewer.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.learn.lavsam.alfatmdbviewer.R
import com.learn.lavsam.alfatmdbviewer.databinding.MainActivityBinding
import com.learn.lavsam.alfatmdbviewer.service.MainBroadcastReceiver

private val receiver = MainBroadcastReceiver()

class MainActivity : AppCompatActivity() {

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

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}