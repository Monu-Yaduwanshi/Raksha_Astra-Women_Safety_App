package com.example.raksha_astra.ui.screens

// HeadlessSmsSendService.kt
//package com.example.raksha_astra.sms

import android.app.Service
import android.content.Intent
import android.os.IBinder

class HeadlessSmsSendService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
