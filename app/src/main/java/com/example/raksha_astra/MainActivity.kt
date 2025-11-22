package com.example.raksha_astra

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.raksha_astra.ui.navigation.AppNavHost
import com.example.raksha_astra.ui.theme.Raksha_AstraTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private var currentDeepLinkId: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        logDeepLinkInfo("onCreate")

        enableEdgeToEdge()

        setContent {
            Raksha_AstraTheme {
                val context = LocalContext.current
                val rootNavController = rememberNavController()
                val deepLinkId = getDeepLinkIdFromIntent(intent)

                // Test buttons for deep links
                if (deepLinkId == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Raksha Astra",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )

                        Button(
                            onClick = {
                                testCustomScheme("test123")
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text("Test Custom Scheme Link")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                testHttpLink("test123")
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text("Test HTTP Link")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                checkDeepLinkSupport()
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text("Check Deep Link Support")
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            "If links don't work, try:\n1. Reinstalling the app\n2. Using HTTP links instead of custom scheme",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                AppNavHost(
                    rootNavController = rootNavController,
                    deepLinkId = deepLinkId
                )
            }
        }

      //   Handle initial deep link
        handleDeepLink(intent)
        val myPackageName = packageName
        if (Telephony.Sms.getDefaultSmsPackage(this) != myPackageName) {
            val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName)
            startActivity(intent) // will prompt user
        }
    }

    // Remove onNewIntent and use this approach instead
    override fun onResume() {
        super.onResume()
        // Check for new deep links when activity resumes
        handleDeepLinkIfNeeded()
    }

    private fun handleDeepLinkIfNeeded() {
        val newDeepLinkId = getDeepLinkIdFromIntent(intent)
        if (!newDeepLinkId.isNullOrEmpty() && newDeepLinkId != currentDeepLinkId) {
            Log.d(TAG, "New deep link detected in onResume: $newDeepLinkId")
            currentDeepLinkId = newDeepLinkId
            showToast("Opening tracking for: $newDeepLinkId")
        }
    }

    private fun handleDeepLink(intent: Intent) {
        val deepLinkId = getDeepLinkIdFromIntent(intent)
        if (!deepLinkId.isNullOrEmpty() && deepLinkId != currentDeepLinkId) {
            Log.d(TAG, "Handling deep link: $deepLinkId")
            currentDeepLinkId = deepLinkId
            showToast("Opening tracking for: $deepLinkId")
        }
    }

    private fun testCustomScheme(shareId: String) {
        val deepLink = "rakshaastra://track/$shareId"
        Log.d(TAG, "Testing Custom Scheme: $deepLink")

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            // Check if there's an app that can handle this intent
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
                showToast("Opening custom scheme link")
            } else {
                showToast("No app can handle this link. The app may not be installed properly.")
                Log.e(TAG, "No activity found to handle: $deepLink")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error testing custom scheme", e)
            showToast("Error: ${e.message}")
        }
    }

    private fun testHttpLink(shareId: String) {
        // Using a free dynamic link service as fallback
        val httpLink = "https://rakshaastra.page.link/track/$shareId"
        Log.d(TAG, "Testing HTTP Link: $httpLink")

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(httpLink)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            showToast("Opening HTTP link")
        } catch (e: Exception) {
            Log.e(TAG, "Error testing HTTP link", e)
            showToast("Error: ${e.message}")
        }
    }

    private fun checkDeepLinkSupport() {
        val testUris = listOf(
            Uri.parse("rakshaastra://track/test123"),
            Uri.parse("https://rakshaastra.page.link/track/test123")
        )

        testUris.forEach { uri ->
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try {
                val activities = packageManager.queryIntentActivities(intent, 0)
                Log.d(TAG, "URI: $uri - Found ${activities.size} handlers")

                activities.forEach { resolveInfo ->
                    Log.d(TAG, "  - ${resolveInfo.activityInfo.packageName}")
                }

                if (activities.isNotEmpty()) {
                    showToast("✓ ${uri.scheme} links supported")
                } else {
                    showToast("✗ No handler for ${uri.scheme}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error checking support for $uri", e)
            }
        }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_LONG).show()
    }

    private fun getDeepLinkIdFromIntent(intent: Intent?): String? {
        if (intent == null) return null

        Log.d(TAG, "Intent action: ${intent.action}")
        Log.d(TAG, "Intent data: ${intent.data}")

        val uri = intent.data ?: return null

        return when {
            // Handle rakshaastra://track/{id}
            uri.scheme == "rakshaastra" && uri.host == "track" -> {
                uri.lastPathSegment
            }
            // Handle https://rakshaastra.page.link/track/{id}
            (uri.scheme == "https" || uri.scheme == "http") &&
                    uri.host == "rakshaastra.page.link" &&
                    uri.pathSegments.firstOrNull() == "track" -> {
                uri.pathSegments.getOrNull(1)
            }
            else -> null
        }
    }

    private fun logDeepLinkInfo(caller: String) {
        val intent = intent
        val data = intent?.data
        val action = intent?.action
        val deepLinkId = getDeepLinkIdFromIntent(intent)

        println("🔗 DEEP LINK DEBUG [$caller]:")
        println("   Action: $action")
        println("   Data: $data")
        println("   DeepLink ID: $deepLinkId")
    }
}