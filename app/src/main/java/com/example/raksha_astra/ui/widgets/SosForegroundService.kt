package com.example.raksha_astra.ui.widgets

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.raksha_astra.R
import com.google.android.gms.location.*
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SosForegroundService : Service() {
    private lateinit var fusedClient: FusedLocationProviderClient
    private var callback: LocationCallback? = null
    private var shareId: String? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        Log.d("SOS_SERVICE", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val commName = intent?.getStringExtra("COMM_NAME") ?: "default"
        Log.d("SOS_SERVICE", "Starting with community: $commName")

        shareId = UUID.randomUUID().toString().replace("-", "").take(12)
        val now = System.currentTimeMillis()
        val expiry = now + 60 * 60 * 1000L // 1 hour

        val db = FirebaseDatabase.getInstance().getReference("live_shares").child(shareId!!)
        val meta = mapOf("createdAt" to now, "expiry" to expiry, "expired" to false)
        db.setValue(meta)

        val notification = buildNotification("Live SOS tracking started")
        startForeground(1, notification)

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(2000L)
            .build()

        callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                val locData = mapOf(
                    "lat" to location.latitude,
                    "lng" to location.longitude,
                    "ts" to System.currentTimeMillis()
                )
                db.child("lastLocation").setValue(locData)
                Log.d("SOS_SERVICE", "Updated location: $locData")
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("SOS_SERVICE", "Location permission missing — stopping service")
            stopSelf()
            return START_NOT_STICKY
        }

        fusedClient.requestLocationUpdates(request, callback!!, null)
        Log.d("SOS_SERVICE", "Location updates started")

        return START_STICKY
    }

    private fun buildNotification(text: String): Notification {
        return NotificationCompat.Builder(this, "sos_channel")
            .setContentTitle("Raksha Astra – SOS Active")
            .setContentText(text)
            .setSmallIcon(R.drawable.rakshaastra)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "sos_channel",
                "SOS Tracking",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callback?.let { fusedClient.removeLocationUpdates(it) }
        shareId?.let {
            FirebaseDatabase.getInstance().getReference("live_shares")
                .child(it).child("expired").setValue(true)
        }
        Log.d("SOS_SERVICE", "Service stopped")
    }

    override fun onBind(intent: android.content.Intent?): IBinder? = null
}
