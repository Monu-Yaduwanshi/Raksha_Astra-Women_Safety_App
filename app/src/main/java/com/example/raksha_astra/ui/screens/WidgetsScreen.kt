package com.example.raksha_astra.ui.screens

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.raksha_astra.R
import com.example.raksha_astra.ui.widgets.SosWidgetProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * Debug function to check widget status
 */
fun debugWidgetStatus(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val provider = ComponentName(context, SosWidgetProvider::class.java)

    try {
        val widgetIds = appWidgetManager.getAppWidgetIds(provider)
        Log.d("WIDGET_DEBUG", "Installed widget IDs: ${widgetIds.joinToString()}")
        Log.d("WIDGET_DEBUG", "Total widgets installed: ${widgetIds.size}")

        if (widgetIds.isNotEmpty()) {
            for (widgetId in widgetIds) {
                val widgetInfo = appWidgetManager.getAppWidgetInfo(widgetId)
                Log.d("WIDGET_DEBUG", "Widget $widgetId info: ${widgetInfo?.provider?.className}")
            }
        } else {
            Log.d("WIDGET_DEBUG", "No widgets installed")
        }
    } catch (e: Exception) {
        Log.e("WIDGET_DEBUG", "Error checking widget status: ${e.message}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var showInstructions by remember { mutableStateOf(false) }
    var showCommunitySelection by remember { mutableStateOf(false) }
    var widgetStatus by remember { mutableStateOf("Checking...") }
    var selectedCommunity by remember { mutableStateOf<Community?>(null) }

    // Load selected community from Firebase when screen loads
    LaunchedEffect(Unit) {
        debugWidgetStatus(context)
        widgetStatus = if (isWidgetAdded(context)) {
            "✅ Widget is installed"
        } else {
            "❌ No widgets installed"
        }
        loadSelectedCommunityFromFirebase(context) { community ->
            selectedCommunity = community
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Widgets",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
//                    Text(
//                        "Raksha Astra Widgets",
//                        color = Color.White,
//                        fontSize = 18.sp
//                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFce2562)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xfffc8967), Color(0xfff84e90))
                    )
                )
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {

                // Title
                Text(
                    text = "SOS Widget Setup",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Selected Community Display
                if (selectedCommunity != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Selected Community",
                                tint = Color(0xFFce2562)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Selected Community:",
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    selectedCommunity?.name ?: "",
                                    color = Color(0xFFce2562),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${selectedCommunity?.members?.size ?: 0} members",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                // Widget Status
                Text(
                    text = widgetStatus,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Widget Icon Button
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clickable {
                            debugWidgetStatus(context)
                            if (isWidgetAdded(context)) {
                                if (selectedCommunity == null) {
                                    showCommunitySelection = true
                                } else {
                                    showInstructions = true
                                }
                            } else {
                                showCommunitySelection = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_sos),
                        contentDescription = "Add SOS Widget",
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = if (isWidgetAdded(context)) {
                        if (selectedCommunity != null) {
                            "SOS Widget is ready! Will alert: ${selectedCommunity?.name}"
                        } else {
                            "Tap to select community for SOS Widget"
                        }
                    } else {
                        "Tap to setup SOS Widget with community"
                    },
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Community Selection Button
                if (!isWidgetAdded(context) || selectedCommunity == null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { showCommunitySelection = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xfff84e90)
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Select Community")
                    }
                }

                // Change Community Button
                if (selectedCommunity != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { showCommunitySelection = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4570F3),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Change Community")
                    }
                }

                // Additional instructions button
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showInstructions = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xfff84e90)
                    )
                ) {
                    Text("Widget Instructions")
                }
            }

            // Community Selection Dialog
            if (showCommunitySelection) {
                WidgetCommunitySelectionDialog(
                    context = context,
                    onDismiss = {
                        showCommunitySelection = false
                    },
                    onCommunitySelected = { community ->
                        showCommunitySelection = false
                        selectedCommunity = community

                        // Save selected community to Firebase for widget use
                        saveSelectedCommunityToFirebase(context, community)

                        // If widget is not installed, prompt to install it
                        if (!isWidgetAdded(context)) {
                            addSosWidget(context)
                        } else {
                            android.widget.Toast.makeText(
                                context,
                                "SOS Widget configured for ${community.name}",
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            }

            // Instructions Dialog
            if (showInstructions) {
                AlertDialog(
                    onDismissRequest = { showInstructions = false },
                    title = {
                        Text("SOS Widget Setup", fontWeight = FontWeight.Bold)
                    },
                    text = {
                        Column {
                            if (selectedCommunity != null) {
                                Text(
                                    "✅ Widget configured for: ${selectedCommunity?.name}",
                                    color = Color.Green,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    "Members: ${selectedCommunity?.members?.size ?: 0}",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            Text("How to use:", fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("• Tap SOS widget to send emergency alert")
                            Text("• Location will be shared with ${selectedCommunity?.name ?: "selected community"}")
                            Text("• Members will receive SMS with tracking link")
                            Text("• Live location updates for 1 hour")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Make sure you have an active internet connection for SOS to work properly.",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showInstructions = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xfff84e90),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Got it!")
                        }
                    }
                )
            }
        }
    }
}

/**
 * Community Selection Dialog for Widget
 */
@Composable
fun WidgetCommunitySelectionDialog(
    context: Context,
    onDismiss: () -> Unit,
    onCommunitySelected: (Community) -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser
    val emailKey = user?.email?.replace(".", "_") ?: "guest"
    val dbRef = FirebaseDatabase.getInstance().getReference("community").child(emailKey)

    var communities by remember { mutableStateOf<Map<String, Community>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(true) }

    // Load communities
    LaunchedEffect(Unit) {
        dbRef.get().addOnSuccessListener { snapshot ->
            val loaded = snapshot.children.associate { comm ->
                val name = comm.key ?: ""
                val community = comm.getValue(Community::class.java) ?: Community(name)
                name to community
            }
            communities = loaded
            isLoading = false
            Log.d("WIDGET_COMMUNITY", "Loaded ${communities.size} communities")
        }.addOnFailureListener {
            isLoading = false
            Log.e("WIDGET_COMMUNITY", "Failed to load communities: ${it.message}")
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Select Community for SOS Widget",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(16.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xfff84e90))
                    }
                } else if (communities.isEmpty()) {
                    Text(
                        "No communities found. Please create a community first in the app.",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    communities.forEach { (name, community) ->
                        Card(
                            onClick = { onCommunitySelected(community) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFf8f8f8)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xfff84e90)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        name,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text(
                                        "${community.members.size} members",
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

/**
 * Save selected community to Firebase for widget use
 */
private fun saveSelectedCommunityToFirebase(context: Context, community: Community) {
    val user = FirebaseAuth.getInstance().currentUser
    val emailKey = user?.email?.replace(".", "_") ?: "guest"

    val dbRef = FirebaseDatabase.getInstance()
        .getReference("widget_settings")
        .child(emailKey)
        .child("selected_community")

    dbRef.setValue(community.name).addOnSuccessListener {
        Log.d("WIDGET_SETUP", "Saved community to Firebase: ${community.name}")
    }.addOnFailureListener { error ->
        Log.e("WIDGET_SETUP", "Failed to save community to Firebase: ${error.message}")
    }
}

/**
 * Load selected community from Firebase
 */
fun loadSelectedCommunityFromFirebase(context: Context, onLoaded: (Community?) -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser
    if (user == null) {
        onLoaded(null)
        return
    }

    val emailKey = user.email?.replace(".", "_") ?: "guest"

    val dbRef = FirebaseDatabase.getInstance()
        .getReference("widget_settings")
        .child(emailKey)
        .child("selected_community")

    dbRef.get().addOnSuccessListener { snapshot ->
        val communityName = snapshot.getValue(String::class.java)
        if (communityName != null) {
            // Now load the complete community data
            loadCommunityDetails(emailKey, communityName, onLoaded)
        } else {
            onLoaded(null)
        }
    }.addOnFailureListener {
        onLoaded(null)
    }
}

/**
 * Load complete community details from Firebase
 */
private fun loadCommunityDetails(userId: String, communityName: String, onLoaded: (Community?) -> Unit) {
    val dbRef = FirebaseDatabase.getInstance()
        .getReference("community")
        .child(userId)
        .child(communityName)

    dbRef.get().addOnSuccessListener { snapshot ->
        val community = snapshot.getValue(Community::class.java)
        if (community != null) {
            Log.d("WIDGET_COMMUNITY", "Loaded community: ${community.name} with ${community.members.size} members")
            onLoaded(community.copy(name = communityName))
        } else {
            Log.e("WIDGET_COMMUNITY", "Community data is null for: $communityName")
            onLoaded(null)
        }
    }.addOnFailureListener { error ->
        Log.e("WIDGET_COMMUNITY", "Failed to load community details: ${error.message}")
        onLoaded(null)
    }
}

/**
 * Get selected community for widget use (called from SosWidgetReceiver)
 */
fun getSelectedCommunityForWidget(context: Context, onLoaded: (Community?) -> Unit) {
    loadSelectedCommunityFromFirebase(context, onLoaded)
}

/**
 * Check if our widget is available/added by checking if any widget IDs exist
 */
fun isWidgetAdded(context: Context): Boolean {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val provider = ComponentName(context, SosWidgetProvider::class.java)

    try {
        val widgetIds = appWidgetManager.getAppWidgetIds(provider)
        return widgetIds.isNotEmpty()
    } catch (e: Exception) {
        return false
    }
}

/**
 * Better approach to add widget programmatically
 */
fun addSosWidget(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val provider = ComponentName(context, SosWidgetProvider::class.java)

    // Method 1: Try direct widget binding (Android 12+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        try {
            val pinIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, provider)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(pinIntent)
            return
        } catch (e: Exception) {
            // Fall through to manual instructions
        }
    }

    // Method 2: Try opening widget picker
    try {
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Method 3: Show manual instructions via toast
        android.widget.Toast.makeText(
            context,
            "Long press home screen → Widgets → Find 'Raksha Astra'",
            android.widget.Toast.LENGTH_LONG
        ).show()

        // Method 4: Open app info page where user can enable widgets
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Last resort - just show toast
        }
    }
}