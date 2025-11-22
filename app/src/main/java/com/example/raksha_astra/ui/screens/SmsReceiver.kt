package com.example.raksha_astra.ui.screens

// SmsReceiver.kt
//package com.example.raksha_astra.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent?.action) {
            // You can log or store received messages if needed
        }
    }
}
