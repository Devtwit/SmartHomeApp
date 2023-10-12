package com.example.bthome

import Service.BleScanService
import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bthome.fragments.AddBleDeviceFragment
import com.example.bthome.fragments.BleScanResultFragment
import com.example.bthome.fragments.MoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class SmartActivity : AppCompatActivity() {
    private val permissionHandler: PermissionHandler by lazy {
        PermissionHandler(this, this)
    }
    private val REQUEST_PERMISSION_CODE = 1
    private val permissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
//        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
    )
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var bottomNavigationView1: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart)



        // complete screen
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        permissionHandler.requestMultiplePermissions(permissions, REQUEST_PERMISSION_CODE)
        permissionHandler.checkStatuses()
        if (permissionHandler.isAllPermissionsEnabled()) {

//            checkBatteryOptimization()
            // Start the foreground service
            val serviceIntent = Intent(this, BleScanService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)

            // Schedule the initial repeating alarm to trigger the service after the specified interval
            scheduleAlarm()
        }
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController

//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        bottomNavigationView.setupWithNavController(navController)
//        fragmentContainer = findViewById(R.id.fragment_container)
//        bottomNavigationView1 = findViewById(R.id.bottomNavigationView)

        // Load the initial fragment
//        loadFragment(AddBleDeviceFragment())
//
//        // Set up bottom navigation item selection listener
//        bottomNavigationView1.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_home -> loadFragment(AddBleDeviceFragment())
//                R.id.navigation_add -> loadFragment(BleScanResultFragment())
//                R.id.navigation_more -> loadFragment(MoreFragment())
//            }
//            true
//        }
    }
//    private fun loadFragment(fragment: Fragment) {
//        val fragmentManager: FragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.fragment_container, fragment)
//        fragmentTransaction.commit()
//    }
    private val REQUEST_BATTERY_OPTIMIZATIONS = 1001

    private fun checkBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = packageName
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                // Request the permission
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, REQUEST_BATTERY_OPTIMIZATIONS)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_BATTERY_OPTIMIZATIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val packageName = packageName
                val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
                if (pm.isIgnoringBatteryOptimizations(packageName)) {
                    // Battery optimization ignored, you can start your continuous scanning here.
                    // Start your Bluetooth scanning service or tasks here.
                } else {
                    // Battery optimization not ignored, you may need to inform the user about
                    // the importance of this permission for continuous scanning.
                }
            }
        }
    }


    private fun scheduleAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, BleScanService::class.java)
        val serviceIntent = Intent(this, BleScanService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + BleScanService.ALARM_INTERVAL_MILLIS,
            BleScanService.ALARM_INTERVAL_MILLIS.toLong(),
            pendingIntent
        )
    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onBackPressed() {
//        super.onBackPressed()
//        fragmentManager.fragments.get(0)
//
//        val count = supportFragmentManager.backStackEntryCount
//
//        if (count == 2) {
////            super.onBackPressed()
//            Log.d("Smart Activity on back pressed","finish() ${fragmentManager.fragments.count()} ${fragmentManager.fragments.size} ${fragmentManager.fragments.get(0)}" )
//            finish()
//            //additional code
//        } else {
//            Log.d("Smart Activity on back pressed","pop backstack ${fragmentManager.fragments.count()} ${fragmentManager.fragments.size} ${fragmentManager.fragments.get(0)}" )
//            supportFragmentManager.popBackStack()
//        }
//
//        Log.d("Smart Activity on back pressed","fragmentManager.fragments.count() ${fragmentManager.fragments.count()} ${fragmentManager.fragments.size} ${fragmentManager.fragments.get(0)}" )
////            if(fragmentManager.fragments.count())
////        finish()
//    }

//    override fun onBackPressed() {
//        val fragment =
//            this.supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
//        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
//            super.onBackPressed()
//        }
//    }
}
