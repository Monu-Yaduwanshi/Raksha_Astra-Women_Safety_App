package com.example.raksha_astra.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmergencyShare
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.raksha_astra.ui.navigation.Screen
import com.example.raksha_astra.ui.theme.PinkDark
import com.example.raksha_astra.ui.theme.PinkLight
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

// ---------------- MAIN HOME SCREEN ----------------
@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val infinite = rememberInfiniteTransition(label = "pulse")
    val pulse by infinite.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ), label = "pulseAnim"
    )

    var sosPressed by remember { mutableStateOf(false) }
    var showCommunityDialog by remember { mutableStateOf(false) }
    val ctx = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xfffc8967), Color(0xfff84e90))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // ✅ Center SOS button vertically
    ) {
        Text(
            "Safety in your hand",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        // Big SOS button (UI unchanged, just added functionality)
//        Box(
//            modifier = Modifier
//                .size(240.dp * pulse)
//                .drawBehind {
//                    // soft radial glow
//                    withTransform({
//                        translate(left = -20f, top = -30f)
//                    }) {
//                        drawCircle(
//                            brush = Brush.radialGradient(
//                                colors = listOf(PinkLight.copy(alpha = 0.45f), Color.Transparent)
//                            ),
//                            radius = size.minDimension / 1.8f
//                        )
//                    }
//                }
//                .background(
//                    brush = Brush.radialGradient(
//                        colors = listOf(MaterialTheme.colorScheme.primary, PinkDark),
//                    ),
//                    shape = CircleShape
//                )
//                .clickable {
//                    sosPressed = true
//                    showCommunityDialog = true // open dialog to choose community
//                },
//            contentAlignment = Alignment.Center
//        ) {
//            Text("SOS", fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color.White)
//        }
        Box(
            modifier = Modifier
                .size(240.dp * pulse)
                .drawBehind {
                    // Soft radial glow effect
                    withTransform({
                        translate(left = -20f, top = -30f)
                    }) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF4570F3).copy(alpha = 0.45f), Color.Transparent)
                            ),
                            radius = size.minDimension / 1.8f
                        )
                    }

                    // Main VERTICAL gradient background with custom distribution
                    drawCircle(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFE006F), // Pink - TOP (40%)
                                Color(0xFFFF4081),
                               // Color(0xFFFE006F), // Pink
                             //   Color(0xffed5d23),
                             //   Color(0xFF067FE9), // Blue - MIDDLE (20%)
                             //   Color(0xffcc2e28),
                             //   Color(0xFFDC143C), // Red - BOTTOM (40%)
                                Color(0xFFF44336),
                                Color(0xFFDC143C)  // Red
                            ),
                            startY = 0f,
                            endY = size.height
                        )
                    )
                }
                .clickable {
                    sosPressed = true
                    showCommunityDialog = true
                }
                .scale(if (sosPressed) 0.95f else 1f)
                .animateContentSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // SOS Icon
                Icon(
                    imageVector = Icons.Filled.EmergencyShare,
                    contentDescription = "SOS Alert",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )

                Spacer(Modifier.height(8.dp))

                // SOS Text
                Text(
                    "SOS",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            if (sosPressed) "SOS sent! Community notified."
            //SOS sent! Contacts & nearby community notified
            else "Hold the SOS button to alert",
            color = if (sosPressed) Color(0xffdc143c) else Color(0xFF4570f3),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,              // slightly bigger
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally) // ✅ Center align text
        )

//        Text(
//            if (sosPressed) "SOS sent! Contacts & nearby community notified."
//            else "Hold the SOS button to alert",
//            color = if (sosPressed) MaterialTheme.colorScheme.primary
//            else MaterialTheme.colorScheme.onSurface,
//            style = MaterialTheme.typography.bodyLarge
//        )

        Spacer(Modifier.height(32.dp))

        // Quick actions row (unchanged)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            AssistChip(
                onClick = {
                    ctx.startActivity(android.content.Intent(ctx, MapActivity::class.java))
                },
                label = { Text("Open Map", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Map, contentDescription = null, tint = Color(0xFFFE006F)) },
                // pinl 0xFFFE006F
                // blue 0xFF067fe9
                border = BorderStroke(4.dp, Color.Black),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )

            AssistChip(
                onClick = {
                    navController.navigate(Screen.LiveShare.route)
                },
                label = { Text("Share Live Location", color = Color.White) },
                leadingIcon = {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = "Share Live Location",
                        tint = Color(0xFFFE006F)
                    )
                },
                border = BorderStroke(4.dp, Color.Black),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )
        }
    }

    // Community dialog trigger
    if (showCommunityDialog) {
        CommunitySelectionDialog(
            context = ctx,
            onDismiss = { showCommunityDialog = false },
            onCommunitySelected = { community ->
                showCommunityDialog = false
                triggerLiveLocationShare(ctx, community)
            }
        )
    }
}

// ---------------- COMMUNITY SELECTION ----------------
@Composable
fun CommunitySelectionDialog(
    context: Context,
    onDismiss: () -> Unit,
    onCommunitySelected: (Community) -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser
    val emailKey = user?.email?.replace(".", "_") ?: "guest"
    val dbRef = FirebaseDatabase.getInstance().getReference("community").child(emailKey)

    var communities by remember { mutableStateOf<Map<String, Community>>(emptyMap()) }

    // Load communities
    LaunchedEffect(Unit) {
        dbRef.get().addOnSuccessListener { snapshot ->
            val loaded = snapshot.children.associate { comm ->
                val name = comm.key ?: ""
                val community = comm.getValue(Community::class.java) ?: Community(name)
                name to community
            }
            communities = loaded
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Select Community", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.height(12.dp))

                communities.forEach { (name, community) ->
                    Button(
                        onClick = { onCommunitySelected(community) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(name)
                    }
                }

                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancel")
                }

//                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
//                    Text("Cancel")
//                }
            }
        }
    }
}

// ---------------- SOS MESSAGE LOGIC ----------------
fun triggerLiveLocationShare(context: Context, community: Community) {
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    fusedClient.lastLocation.addOnSuccessListener { location ->
        val db = FirebaseDatabase.getInstance().getReference("live_locations")

        val shareId = UUID.randomUUID().toString().take(8)
        val expiryTime = System.currentTimeMillis() + 3600000 // 1 hour
        val expiryDate = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(expiryTime))

        val data = mapOf(
            "lat" to (location?.latitude ?: 0.0),
            "lng" to (location?.longitude ?: 0.0),
            "timestamp" to System.currentTimeMillis(),
            "expiry" to expiryTime
        )

        db.child(shareId).setValue(data).addOnSuccessListener {
            val simpleDeepLink = "https://raksha-astra.page.link/track?shareId=$shareId"

            val message = """
                I'm sharing my live location for the next hour.

                📍 Tracking ID: $shareId
                ⏰ Expires: $expiryDate

                📱 TO TRACK MY LOCATION:
                1. Open Raksha Astra app
                2. Go to 'Live Share' screen  
                3. Enter this Tracking ID: $shareId
                4. Click Search to start tracking

                🔗 OR CLICK LINK:
                $simpleDeepLink

                🔒 Secure sharing via Raksha Astra
            """.trimIndent()

            val recipients = community.members.values
                .map { formatPhoneNumber(it.phone) }
                .filter { it.isNotBlank() }
                .joinToString(";")

            if (recipients.isBlank()) {
                Toast.makeText(context, "No valid phone numbers found", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

            try {
                val smsUri = Uri.parse("smsto:$recipients")
                val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
                    putExtra("sms_body", message)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                Log.d("HOME_SOS", "✓ Live share sent to: $recipients")
            } catch (e: Exception) {
                Log.e("HOME_SOS", "✗ Failed: ${e.message}")
                Toast.makeText(context, "Failed to open SMS app", Toast.LENGTH_LONG).show()
            }
        }
    }
}
