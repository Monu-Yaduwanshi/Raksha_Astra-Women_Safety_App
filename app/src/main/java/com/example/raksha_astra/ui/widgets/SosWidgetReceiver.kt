package com.example.raksha_astra.ui.widgets

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.raksha_astra.ui.screens.getSelectedCommunityForWidget
import com.example.raksha_astra.ui.screens.triggerLiveLocationShare
import com.google.firebase.auth.FirebaseAuth

class SosWidgetReceiver : BroadcastReceiver() {
    @RequiresApi(android.os.Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("SOS_WIDGET", "=== SOS WIDGET RECEIVER STARTED ===")
        Log.d("SOS_WIDGET", "Broadcast received: ${intent?.action}")

        if (intent?.action == SosWidgetProvider.ACTION_SOS_TRIGGER) {
            Log.d("SOS_WIDGET", "SOS TRIGGER CONFIRMED - Starting emergency process")
            Toast.makeText(context, "🚨 SOS triggered from widget!", Toast.LENGTH_SHORT).show()

            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Log.e("SOS_WIDGET", "User not logged in - cannot trigger SOS")
                Toast.makeText(context, "Please login to use SOS", Toast.LENGTH_SHORT).show()
                return
            }

            // Get the selected community from Firebase
            getSelectedCommunityForWidget(context) { community ->
                if (community == null) {
                    Log.e("SOS_WIDGET", "No community selected for widget")
                    Toast.makeText(
                        context,
                        "Please setup SOS widget first - select a community in Widgets screen",
                        Toast.LENGTH_LONG
                    ).show()
                    return@getSelectedCommunityForWidget
                }

                if (community.members.isEmpty()) {
                    Log.e("SOS_WIDGET", "No members in selected community")
                    Toast.makeText(
                        context,
                        "No members in ${community.name}. Please add members first.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@getSelectedCommunityForWidget
                }

                Log.d("SOS_WIDGET", "Using community: ${community.name} with ${community.members.size} members")

                // Debug: Log all members and their phone numbers
                community.members.forEach { (key, member) ->
                    Log.d("SOS_WIDGET", "Member: ${member.name} - Phone: ${member.phone}")
                }

                // Use the same function as HomeScreen to trigger live location share
                triggerLiveLocationShare(context, community)

                Log.d("SOS_WIDGET", "SOS emergency triggered for community: ${community.name}")
                Toast.makeText(context, "SOS sent to ${community.name}!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("SOS_WIDGET", "Unknown action received: ${intent?.action}")
        }
        Log.d("SOS_WIDGET", "=== SOS WIDGET RECEIVER FINISHED ===")
    }
}