//package com.example.raksha_astra.ui.screens
//
//import android.Manifest
//import android.content.Intent
//import android.net.Uri
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.MapView
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//
//@Composable
//fun LiveLocationScreen() {
//    val context = LocalContext.current
//    var mapView: MapView? by remember { mutableStateOf(null) }
//    var lastLatLng by remember { mutableStateOf<LatLng?>(null) }
//    var showShareDialog by remember { mutableStateOf(false) }
//
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//    // 🔹 Permission launcher
//    val locationPermissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { perms ->
//        if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
//            perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
//        ) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//                loc?.let {
//                    lastLatLng = LatLng(it.latitude, it.longitude)
//                    mapView?.getMapAsync { googleMap ->
//                        googleMap.isMyLocationEnabled = true
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng!!, 16f))
//                        googleMap.addMarker(MarkerOptions().position(lastLatLng!!).title("You"))
//                    }
//                }
//            }
//        } else {
//            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(listOf(Color(0xfffc8967), Color(0xfff84e90)))
////                Brush.verticalGradient(
////                    listOf(Color(0xFF4570F3), Color(0xFFFE006F)) // Blue → Pink
////                )
//            )
//            .padding(12.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
////        Text(
////            "Live Location Sharing",
////            style = MaterialTheme.typography.headlineSmall,
////            color = Color.White
////        )
//        Text(
//            "Live Location Sharing",
//            style = MaterialTheme.typography.headlineSmall,
//            color = Color.White,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(Modifier.height(12.dp))
//
//        // 🔹 MapView inside Compose
//        AndroidView(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .padding(8.dp),
//            factory = { ctx ->
//                MapView(ctx).apply {
//                    onCreate(null)
//                    getMapAsync { googleMap: GoogleMap ->
//                        googleMap.uiSettings.isZoomControlsEnabled = true
//                        googleMap.uiSettings.isMyLocationButtonEnabled = true
//                    }
//                    mapView = this
//                }
//            },
//            update = { it.onResume() }
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                locationPermissionLauncher.launch(
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    )
//                )
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF4570F3), // Pink
//                contentColor = Color.White
//            ),
//            modifier = Modifier.fillMaxWidth(0.8f)
//        ) {
//            Text("Get My Location")
//        }
//
//        Spacer(Modifier.height(12.dp))
//
//        Button(
//            onClick = { showShareDialog = true },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF4570F3), // Blue
//                contentColor = Color.White
//            ),
//            modifier = Modifier.fillMaxWidth(0.8f)
//        ) {
//            Text("Share My Location")
//        }
//    }
//
//    // 🔹 Share Dialog
//    if (showShareDialog) {
//        AlertDialog(
//            onDismissRequest = { showShareDialog = false },
//            title = { Text("Share Location") },
//            text = { Text("Choose how you want to share your location") },
//            confirmButton = {
//                Column {
//                    Button(
//                        onClick = {
//                            showShareDialog = false
//                            Toast.makeText(context, "Shared with Community (UI only)", Toast.LENGTH_SHORT).show()
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Share to Community")
//                    }
//
//                    Spacer(Modifier.height(8.dp))
//
//                    Button(
//                        onClick = {
//                            showShareDialog = false
//                            lastLatLng?.let {
//                                val gmapsLink = "https://www.google.com/maps?q=${it.latitude},${it.longitude}"
//                                val intent = Intent(Intent.ACTION_SEND).apply {
//                                    type = "text/plain"
//                                    putExtra(Intent.EXTRA_TEXT, "Track me here: $gmapsLink")
//                                }
//                                context.startActivity(Intent.createChooser(intent, "Share via"))
//                            } ?: Toast.makeText(context, "Get location first", Toast.LENGTH_SHORT).show()
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Share via Apps")
//                    }
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showShareDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//}
//
//
//package com.example.raksha_astra.ui.screens
//
//import android.Manifest
//import android.content.Intent
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import com.google.android.gms.location.*
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.MapView
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.firebase.database.FirebaseDatabase
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import java.util.*
//
//@Composable
//fun LiveLocationScreen() {
//    val context = LocalContext.current
//    var mapView: MapView? by remember { mutableStateOf(null) }
//    var lastLatLng by remember { mutableStateOf<LatLng?>(null) }
//    var showShareDialog by remember { mutableStateOf(false) }
//
//    // New state variables for search functionality
//    var searchText by remember { mutableStateOf("") }
//    var isSearching by remember { mutableStateOf(false) }
//    var trackedLocation by remember { mutableStateOf<LatLng?>(null) }
//    var trackedShareId by remember { mutableStateOf<String?>(null) }
//
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    val locationRequest = remember {
//        LocationRequest.create().apply {
//            interval = 5000L
//            fastestInterval = 2000L
//            priority = Priority.PRIORITY_HIGH_ACCURACY
//        }
//    }
//
//    val coroutineScope = rememberCoroutineScope()
//    val sharingJobRef = remember { mutableStateOf<Job?>(null) }
//    val locationCallbackRef = remember { mutableStateOf<LocationCallback?>(null) }
//    val database = FirebaseDatabase.getInstance().reference
//
//    val locationPermissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { perms ->
//        if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
//            perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
//        ) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//                loc?.let {
//                    lastLatLng = LatLng(it.latitude, it.longitude)
//                    mapView?.getMapAsync { googleMap ->
//                        try {
//                            googleMap.isMyLocationEnabled = true
//                        } catch (_: SecurityException) {}
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng!!, 16f))
//                        googleMap.addMarker(MarkerOptions().position(lastLatLng!!).title("You"))
//                    }
//                }
//            }
//        } else {
//            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Function to start tracking a shared location
//    fun startTrackingLocation(shareId: String) {
//        if (shareId.isBlank()) {
//            Toast.makeText(context, "Please enter a valid tracking ID", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        isSearching = true
//        trackedShareId = shareId
//
//        // Listen for location updates from Firebase
//        val locationListener = object : com.google.firebase.database.ValueEventListener {
//            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
//                isSearching = false
//
//                if (!snapshot.exists()) {
//                    Toast.makeText(context, "Tracking ID not found: $shareId", Toast.LENGTH_SHORT).show()
//                    return
//                }
//
//                val expired = snapshot.child("expired").getValue(Boolean::class.java) ?: true
//
//                if (expired) {
//                    Toast.makeText(context, "This location sharing has expired", Toast.LENGTH_SHORT).show()
//                    return
//                }
//
//                val locationData = snapshot.child("lastLocation").getValue(LocationData::class.java)
//                if (locationData == null) {
//                    Toast.makeText(context, "No location data available yet", Toast.LENGTH_SHORT).show()
//                    return
//                }
//
//                val newLatLng = LatLng(locationData.lat, locationData.lng)
//                trackedLocation = newLatLng
//
//                mapView?.getMapAsync { googleMap ->
//                    googleMap.clear()
//
//                    // Add marker for tracked location
//                    googleMap.addMarker(
//                        MarkerOptions()
//                            .position(newLatLng)
//                            .title("Tracked Location")
//                            .snippet("Tracking ID: $shareId")
//                    )
//
//                    // Add marker for current user location if available
//                    lastLatLng?.let { userLoc ->
//                        googleMap.addMarker(
//                            MarkerOptions()
//                                .position(userLoc)
//                                .title("Your Location")
//                        )
//                    }
//
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 14f))
//                    Toast.makeText(context, "Tracking active! Location updates every 5 seconds", Toast.LENGTH_LONG).show()
//                }
//            }
//
//            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
//                isSearching = false
//                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        database.child("live_shares").child(shareId).addValueEventListener(locationListener)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(listOf(Color(0xfffc8967), Color(0xfff84e90)))
//            )
//            .padding(12.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            "Live Location Sharing",
//            style = MaterialTheme.typography.headlineSmall,
//            color = Color.White,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(Modifier.height(12.dp))
//
//        // Search Bar for Tracking
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(12.dp)
//            ) {
//                Text(
//                    "Track Someone's Location",
//                    style = MaterialTheme.typography.labelMedium,
//                    color = Color.Black,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    OutlinedTextField(
//                        value = searchText,
//                        onValueChange = { searchText = it },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(end = 8.dp),
//                        placeholder = { Text("Enter Tracking ID") },
//                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                        colors = TextFieldDefaults.colors(
//                            focusedContainerColor = Color.Transparent,
//                            unfocusedContainerColor = Color.Transparent,
//                            disabledContainerColor = Color.Transparent,
//                        )
//                    )
//
//                    Button(
//                        onClick = {
//                            startTrackingLocation(searchText.trim())
//                        },
//                        enabled = searchText.isNotBlank() && !isSearching,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFF4570F3),
//                            contentColor = Color.White
//                        )
//                    ) {
//                        if (isSearching) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(20.dp),
//                                color = Color.White,
//                                strokeWidth = 2.dp
//                            )
//                        } else {
//                            Icon(
//                                imageVector = Icons.Default.Search,
//                                contentDescription = "Track"
//                            )
//                        }
//                    }
//                }
//
//                Text(
//                    "Enter the Tracking ID shared with you",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray,
//                    modifier = Modifier.padding(top = 8.dp)
//                )
//            }
//        }
//
//        Spacer(Modifier.height(12.dp))
//
//        // Show tracking status
//        if (isSearching && trackedShareId != null) {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.8f))
//            ) {
//                Text(
//                    text = "🔍 Tracking: ${trackedShareId}",
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(12.dp)
//                )
//            }
//            Spacer(Modifier.height(8.dp))
//        }
//
//        // MapView
//        AndroidView(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .padding(8.dp),
//            factory = { ctx ->
//                MapView(ctx).apply {
//                    onCreate(null)
//                    getMapAsync { googleMap: GoogleMap ->
//                        googleMap.uiSettings.isZoomControlsEnabled = true
//                        googleMap.uiSettings.isMyLocationButtonEnabled = true
//                    }
//                    mapView = this
//                }
//            },
//            update = { it.onResume() }
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                locationPermissionLauncher.launch(
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    )
//                )
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF4570F3),
//                contentColor = Color.White
//            ),
//            modifier = Modifier.fillMaxWidth(0.8f)
//        ) {
//            Text("Get My Location")
//        }
//
//        Spacer(Modifier.height(12.dp))
//
//        Button(
//            onClick = { showShareDialog = true },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF4570F3),
//                contentColor = Color.White
//            ),
//            modifier = Modifier.fillMaxWidth(0.8f)
//        ) {
//            Text("Share My Location")
//        }
//    }
//
//    // Share Dialog
//    if (showShareDialog) {
//        AlertDialog(
//            onDismissRequest = { showShareDialog = false },
//            title = { Text("Share Location") },
//            text = { Text("Choose how you want to share your location") },
//            confirmButton = {
//                Column {
//                    Button(
//                        onClick = {
//                            showShareDialog = false
//                            Toast.makeText(context, "Shared with Community (UI only)", Toast.LENGTH_SHORT).show()
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Share to Community")
//                    }
//
//                    Spacer(Modifier.height(8.dp))
//
//                    Button(
//                        onClick = {
//                            showShareDialog = false
//
//                            val hasFine = androidx.core.content.ContextCompat.checkSelfPermission(
//                                context, Manifest.permission.ACCESS_FINE_LOCATION
//                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
//                            val hasCoarse = androidx.core.content.ContextCompat.checkSelfPermission(
//                                context, Manifest.permission.ACCESS_COARSE_LOCATION
//                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
//
//                            if (!hasFine && !hasCoarse) {
//                                Toast.makeText(context, "Please grant location permission first", Toast.LENGTH_SHORT).show()
//                                locationPermissionLauncher.launch(
//                                    arrayOf(
//                                        Manifest.permission.ACCESS_FINE_LOCATION,
//                                        Manifest.permission.ACCESS_COARSE_LOCATION
//                                    )
//                                )
//                                return@Button
//                            }
//
//                            // Create live share ID
//                            val shareId = UUID.randomUUID().toString().replace("-", "").take(12)
//                            val now = System.currentTimeMillis()
//                            val expiry = now + 60 * 60 * 1000L // 1 hour
//
//                            // Save initial location
//                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                                location?.let { loc ->
//                                    val locObj = mapOf(
//                                        "lat" to loc.latitude,
//                                        "lng" to loc.longitude,
//                                        "ts" to System.currentTimeMillis()
//                                    )
//
//                                    val shareMeta = mapOf(
//                                        "createdAt" to now,
//                                        "expiry" to expiry,
//                                        "expired" to false,
//                                        "public" to true,
//                                        "lastLocation" to locObj
//                                    )
//
//                                    database.child("live_shares").child(shareId).setValue(shareMeta)
//                                        .addOnSuccessListener {
//                                            Toast.makeText(context, "Location sharing started! Share ID: $shareId", Toast.LENGTH_LONG).show()
//                                        }
//                                        .addOnFailureListener {
//                                            Toast.makeText(context, "Failed to start sharing", Toast.LENGTH_SHORT).show()
//                                        }
//                                }
//                            }
//
//                            // Start continuous location updates
//                            locationCallbackRef.value?.let { fusedLocationClient.removeLocationUpdates(it) }
//                            val locationCallback = object : LocationCallback() {
//                                override fun onLocationResult(result: LocationResult) {
//                                    super.onLocationResult(result)
//                                    val location = result.lastLocation ?: return
//                                    lastLatLng = LatLng(location.latitude, location.longitude)
//
//                                    // Update Firebase with new location
//                                    val locObj = mapOf(
//                                        "lat" to location.latitude,
//                                        "lng" to location.longitude,
//                                        "ts" to System.currentTimeMillis()
//                                    )
//                                    database.child("live_shares").child(shareId).child("lastLocation")
//                                        .setValue(locObj)
//
//                                    mapView?.getMapAsync { googleMap ->
//                                        try {
//                                            googleMap.isMyLocationEnabled = true
//                                        } catch (_: SecurityException) {}
//                                        googleMap.clear()
//                                        googleMap.addMarker(
//                                            MarkerOptions().position(lastLatLng!!).title("You (Sharing)")
//                                        )
//                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng!!, 16f))
//                                    }
//                                }
//                            }
//
//                            try {
//                                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
//                                locationCallbackRef.value = locationCallback
//                            } catch (se: SecurityException) {
//                                Toast.makeText(context, "Location permission missing", Toast.LENGTH_SHORT).show()
//                                return@Button
//                            }
//
//                            // Auto-expire after 1 hour
//                            val job = coroutineScope.launch {
//                                val remaining = expiry - System.currentTimeMillis()
//                                if (remaining > 0) delay(remaining)
//
//                                locationCallbackRef.value?.let { fusedLocationClient.removeLocationUpdates(it) }
//                                locationCallbackRef.value = null
//                                database.child("live_shares").child(shareId).child("expired").setValue(true)
//
//                                Toast.makeText(context, "Location sharing expired", Toast.LENGTH_SHORT).show()
//                            }
//
//                            sharingJobRef.value?.cancel()
//                            sharingJobRef.value = job
//
//                            // Generate shareable links
//                            val expiryDate = android.text.format.DateFormat.format("dd MMM yyyy HH:mm", Date(expiry))
//
//                            val simpleDeepLink = "rakshaastra://track/$shareId"
//                            val webLink = "https://rakshaastra.com/track/$shareId"
//
//                            val shareText = """
//🚨 LIVE LOCATION SHARING 🚨
//
//I'm sharing my live location for the next hour.
//
//📍 Tracking ID: $shareId
//⏰ Expires: $expiryDate
//
//📱 TO TRACK MY LOCATION:
//1. Open Raksha Astra app
//2. Go to 'Live Share' screen
//3. Enter this Tracking ID: $shareId
//4. Click Search to start tracking
//
//🔗 OR CLICK LINK:
//$simpleDeepLink
//
//🔒 Secure sharing via Raksha Astra
//""".trimIndent()
//
//                            val intent = Intent(Intent.ACTION_SEND).apply {
//                                type = "text/plain"
//                                putExtra(Intent.EXTRA_TEXT, shareText)
//                                putExtra(Intent.EXTRA_SUBJECT, "📍 Live Location Sharing")
//                            }
//
//                            context.startActivity(Intent.createChooser(intent, "Share Location via"))
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Share via Apps")
//                    }
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showShareDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//
//    // Cleanup
//    DisposableEffect(Unit) {
//        onDispose {
//            locationCallbackRef.value?.let {
//                fusedLocationClient.removeLocationUpdates(it)
//            }
//            sharingJobRef.value?.cancel()
//        }
//    }
//}
//
//data class LocationData(
//    val lat: Double = 0.0,
//    val lng: Double = 0.0,
//    val ts: Long = 0L
//)







package com.example.raksha_astra.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun LiveLocationScreen() {
    val context = LocalContext.current
    var mapView: MapView? by remember { mutableStateOf(null) }
    var lastLatLng by remember { mutableStateOf<LatLng?>(null) }
    var showShareDialog by remember { mutableStateOf(false) }

    // New state variables for search functionality
    var searchText by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var trackedLocation by remember { mutableStateOf<LatLng?>(null) }
    var trackedShareId by remember { mutableStateOf<String?>(null) }

    // --- NEW: Communities state ---
    var communities by remember { mutableStateOf<Map<String, Community>>(emptyMap()) }
    var showCommunityPicker by remember { mutableStateOf(false) }
    var communityToShare by remember { mutableStateOf<Pair<String, Community>?>(null) } // name to Community


    // transient holder for the built share message to be edited (used after selecting community)
    var selectedShareMessageTemp by remember { mutableStateOf<String?>(null) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = remember {
        LocationRequest.create().apply {
            interval = 5000L
            fastestInterval = 2000L
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val sharingJobRef = remember { mutableStateOf<Job?>(null) }
    val locationCallbackRef = remember { mutableStateOf<LocationCallback?>(null) }
    val database = FirebaseDatabase.getInstance().reference

    // Load user's communities on composition (same path as CommunityScreen)
    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        val emailKey = user?.email?.replace(".", "_") ?: "guest"
        val dbRef = FirebaseDatabase.getInstance().getReference("community").child(emailKey)
        dbRef.get().addOnSuccessListener { snapshot ->
            val loaded = snapshot.children.associate { comm ->
                val name = comm.key ?: ""
                val community = comm.getValue(Community::class.java) ?: Community(name)
                val members = community.members ?: emptyMap()
                name to community.copy(members = members)
            }
            communities = loaded
        }.addOnFailureListener {
            // Keep empty on failure
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let {
                    lastLatLng = LatLng(it.latitude, it.longitude)
                    mapView?.getMapAsync { googleMap ->
                        try {
                            googleMap.isMyLocationEnabled = true
                        } catch (_: SecurityException) {}
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng!!, 16f))
                        googleMap.addMarker(MarkerOptions().position(lastLatLng!!).title("You"))
                    }
                }
            }
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to start tracking a shared location
    fun startTrackingLocation(shareId: String) {
        if (shareId.isBlank()) {
            Toast.makeText(context, "Please enter a valid tracking ID", Toast.LENGTH_SHORT).show()
            return
        }

        isSearching = true
        trackedShareId = shareId

        // Listen for location updates from Firebase
        val locationListener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                isSearching = false

                if (!snapshot.exists()) {
                    Toast.makeText(context, "Tracking ID not found: $shareId", Toast.LENGTH_SHORT).show()
                    return
                }

                val expired = snapshot.child("expired").getValue(Boolean::class.java) ?: true

                if (expired) {
                    Toast.makeText(context, "This location sharing has expired", Toast.LENGTH_SHORT).show()
                    return
                }

                val locationData = snapshot.child("lastLocation").getValue(LocationData::class.java)
                if (locationData == null) {
                    Toast.makeText(context, "No location data available yet", Toast.LENGTH_SHORT).show()
                    return
                }

                val newLatLng = LatLng(locationData.lat, locationData.lng)
                trackedLocation = newLatLng

                mapView?.getMapAsync { googleMap ->
                    googleMap.clear()

                    // Add marker for tracked location
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(newLatLng)
                            .title("Tracked Location")
                            .snippet("Tracking ID: $shareId")
                    )

                    // Add marker for current user location if available
                    lastLatLng?.let { userLoc ->
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(userLoc)
                                .title("Your Location")
                        )
                    }

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 14f))
                    Toast.makeText(context, "Tracking active! Location updates every 5 seconds", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                isSearching = false
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        database.child("live_shares").child(shareId).addValueEventListener(locationListener)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xfffc8967), Color(0xfff84e90)))
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Live Location Sharing",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        // Search Bar for Tracking
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    "Track Someone's Location",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        placeholder = { Text("Enter Tracking ID") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        )
                    )

                    Button(
                        onClick = {
                            startTrackingLocation(searchText.trim())
                        },
                        enabled = searchText.isNotBlank() && !isSearching,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4570F3),
                            contentColor = Color.White
                        )
                    ) {
                        if (isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Track"
                            )
                        }
                    }
                }

                Text(
                    "Enter the Tracking ID shared with you",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Show tracking status
        if (isSearching && trackedShareId != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.8f))
            ) {
                Text(
                    text = "🔍 Tracking: ${trackedShareId}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
        }

        // MapView
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            factory = { ctx ->
                MapView(ctx).apply {
                    onCreate(null)
                    getMapAsync { googleMap: GoogleMap ->
                        googleMap.uiSettings.isZoomControlsEnabled = true
                        googleMap.uiSettings.isMyLocationButtonEnabled = true
                    }
                    mapView = this
                }
            },
            update = { it.onResume() }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4570F3),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Get My Location")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { showShareDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4570F3),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Share My Location")
        }
    }

    // -------- Share Dialog (original) --------
    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            title = { Text("Share Location") },
            text = { Text("Choose how you want to share your location") },
            confirmButton = {
                Column {
                    Button(
                        onClick = {
                            // NEW: show community picker
                            showShareDialog = false
                            // load communities are already loaded in state; show picker
                            showCommunityPicker = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Share to Community")
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            showShareDialog = false

                            val hasFine = androidx.core.content.ContextCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                            val hasCoarse = androidx.core.content.ContextCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                            if (!hasFine && !hasCoarse) {
                                Toast.makeText(context, "Please grant location permission first", Toast.LENGTH_SHORT).show()
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                                return@Button
                            }

                            // Create live share ID
                            val shareId = UUID.randomUUID().toString().replace("-", "").take(12)
                            val now = System.currentTimeMillis()
                            val expiry = now + 60 * 60 * 1000L // 1 hour

                            // Save initial location
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                location?.let { loc ->
                                    val locObj = mapOf(
                                        "lat" to loc.latitude,
                                        "lng" to loc.longitude,
                                        "ts" to System.currentTimeMillis()
                                    )

                                    val shareMeta = mapOf(
                                        "createdAt" to now,
                                        "expiry" to expiry,
                                        "expired" to false,
                                        "public" to true,
                                        "lastLocation" to locObj
                                    )

                                    database.child("live_shares").child(shareId).setValue(shareMeta)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Location sharing started! Share ID: $shareId", Toast.LENGTH_LONG).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Failed to start sharing", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }

                            // Start continuous location updates
                            locationCallbackRef.value?.let { fusedLocationClient.removeLocationUpdates(it) }
                            val locationCallback = object : LocationCallback() {
                                override fun onLocationResult(result: LocationResult) {
                                    super.onLocationResult(result)
                                    val location = result.lastLocation ?: return
                                    lastLatLng = LatLng(location.latitude, location.longitude)

                                    // Update Firebase with new location
                                    val locObj = mapOf(
                                        "lat" to location.latitude,
                                        "lng" to location.longitude,
                                        "ts" to System.currentTimeMillis()
                                    )
                                    database.child("live_shares").child(shareId).child("lastLocation")
                                        .setValue(locObj)

                                    mapView?.getMapAsync { googleMap ->
                                        try {
                                            googleMap.isMyLocationEnabled = true
                                        } catch (_: SecurityException) {}
                                        googleMap.clear()
                                        googleMap.addMarker(
                                            MarkerOptions().position(lastLatLng!!).title("You (Sharing)")
                                        )
                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng!!, 16f))
                                    }
                                }
                            }

                            try {
                                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                                locationCallbackRef.value = locationCallback
                            } catch (se: SecurityException) {
                                Toast.makeText(context, "Location permission missing", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // Auto-expire after 1 hour
                            val job = coroutineScope.launch {
                                val remaining = expiry - System.currentTimeMillis()
                                if (remaining > 0) delay(remaining)

                                locationCallbackRef.value?.let { fusedLocationClient.removeLocationUpdates(it) }
                                locationCallbackRef.value = null
                                database.child("live_shares").child(shareId).child("expired").setValue(true)

                                Toast.makeText(context, "Location sharing expired", Toast.LENGTH_SHORT).show()
                            }

                            sharingJobRef.value?.cancel()
                            sharingJobRef.value = job

                            // Generate shareable links
                            val expiryDate = android.text.format.DateFormat.format("dd MMM yyyy HH:mm", Date(expiry))

                            val simpleDeepLink = "rakshaastra://track/$shareId"
                            val webLink = "https://rakshaastra.com/track/$shareId"

                            val shareText = """
🚨 LIVE LOCATION SHARING 🚨

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

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                putExtra(Intent.EXTRA_SUBJECT, "📍 Live Location Sharing")
                            }

                            context.startActivity(Intent.createChooser(intent, "Share Location via"))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Share via Apps")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // -------- Community Picker Dialog (NEW) --------
    if (showCommunityPicker) {
        AlertDialog(
            onDismissRequest = { showCommunityPicker = false },
            title = { Text("Select Community to Share") },
            text = {
                if (communities.isEmpty()) {
                    Text("No communities found for your account.")
                } else {
                    Column {
                        Text("Choose the community to which you'd like to send the live share info:")
                        Spacer(Modifier.height(8.dp))
                        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                            items(communities.entries.toList()) { (name, comm) ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(name, fontWeight = FontWeight.Bold, color = Color.Black)
                                            Text("Members: ${comm.members.size}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                        }
                                        Button(onClick = {
                                            // when community selected, we need to create a shareId + message then show confirm/edit dialog
                                            showCommunityPicker = false

                                            // Create shareId and store meta exactly like the 'Share via Apps' flow
                                            val shareId = UUID.randomUUID().toString().replace("-", "").take(12)
                                            val now = System.currentTimeMillis()
                                            val expiry = now + 60 * 60 * 1000L // 1 hour

                                            // Save initial location (if available)
                                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                                location?.let { loc ->
                                                    val locObj = mapOf(
                                                        "lat" to loc.latitude,
                                                        "lng" to loc.longitude,
                                                        "ts" to System.currentTimeMillis()
                                                    )

                                                    val shareMeta = mapOf(
                                                        "createdAt" to now,
                                                        "expiry" to expiry,
                                                        "expired" to false,
                                                        "public" to true,
                                                        "lastLocation" to locObj
                                                    )

                                                    database.child("live_shares").child(shareId).setValue(shareMeta)
                                                }
                                            }

                                            // Start continuous location updates for this share
                                            locationCallbackRef.value?.let { fusedLocationClient.removeLocationUpdates(it) }
                                            val locationCallback = object : LocationCallback() {
                                                override fun onLocationResult(result: LocationResult) {
                                                    super.onLocationResult(result)
                                                    val location = result.lastLocation ?: return
                                                    lastLatLng = LatLng(location.latitude, location.longitude)

                                                    val locObj = mapOf(
                                                        "lat" to location.latitude,
                                                        "lng" to location.longitude,
                                                        "ts" to System.currentTimeMillis()
                                                    )
                                                    database.child("live_shares").child(shareId).child("lastLocation")
                                                        .setValue(locObj)

                                                    mapView?.getMapAsync { googleMap ->
                                                        try {
                                                            googleMap.isMyLocationEnabled = true
                                                        } catch (_: SecurityException) {}
                                                        googleMap.clear()
                                                        mapView?.getMapAsync { gm ->
                                                            gm.addMarker(MarkerOptions().position(lastLatLng!!).title("You (Sharing)"))
                                                            gm.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng!!, 16f))
                                                        }
                                                    }
                                                }
                                            }

                                            try {
                                                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                                                locationCallbackRef.value = locationCallback
                                            } catch (se: SecurityException) {
                                                Toast.makeText(context, "Location permission missing", Toast.LENGTH_SHORT).show()
                                            }

                                            // Auto-expire after 1 hour (same as other flow)
                                            val job = coroutineScope.launch {
                                                val remaining = expiry - System.currentTimeMillis()
                                                if (remaining > 0) delay(remaining)

                                                locationCallbackRef.value?.let { fusedLocationClient.removeLocationUpdates(it) }
                                                locationCallbackRef.value = null
                                                database.child("live_shares").child(shareId).child("expired").setValue(true)

                                                Toast.makeText(context, "Location sharing expired", Toast.LENGTH_SHORT).show()
                                            }

                                            sharingJobRef.value?.cancel()
                                            sharingJobRef.value = job

                                            // Build the share message and open edit/confirm dialog
                                            val expiryDate = android.text.format.DateFormat.format("dd MMM yyyy HH:mm", Date(expiry))
                                            val simpleDeepLink = "rakshaastra://track/$shareId"

                                            val shareText = """
🚨 LIVE LOCATION SHARING 🚨

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

                                            // store the selected community + prebuilt message for editing and confirmation
                                            communityToShare = name to comm
                                            // show edit dialog after setting community and message (we will hold the message in the dialog state)
                                            // We create the dialog by setting a state flag below
                                            // To keep things simple: we'll reuse communityToShare non-null as trigger for the edit dialog
                                            // The actual message will be passed into the dialog composable
                                            // Save the shareText as well by pairing it in a transient state var
                                            selectedShareMessageTemp = shareText
                                        }) {
                                            Text("Select")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCommunityPicker = false }) {
                    Text("Close")
                }
            }
        )
    }

    // transient holder for the built share message to be edited (used after selecting community)
    //   var selectedShareMessageTemp by remember { mutableStateOf<String?>(null) }

    // -------- Edit & Confirm Share Message (NEW) --------
    communityToShare?.let { (commName, comm) ->
        // Only show if we have a message prepared
        val prefill = selectedShareMessageTemp ?: ""
        if (prefill.isNotBlank()) {
            EditShareMessageDialog(
                initialMessage = prefill,
                commName = commName,
                onDismiss = {
                    communityToShare = null
                    selectedShareMessageTemp = null
                },
                onConfirm = { editedMessage ->
                    // Send the editedMessage to every member of selected community
                    sendMessageToCommunity(context, comm, editedMessage)
                    Toast.makeText(context, "Message sent to ${comm.members.size} members in $commName", Toast.LENGTH_LONG).show()

                    // cleanup
                    communityToShare = null
                    selectedShareMessageTemp = null
                }
            )
        }
    }

    // cleanup: if user selected from picker but we didn't set communityToShare, handle it
    if (selectedShareMessageTemp != null && communityToShare == null) {
        // nothing to do: keep the message until communityToShare is set or user cancels
    }

    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            locationCallbackRef.value?.let {
                fusedLocationClient.removeLocationUpdates(it)
            }
            sharingJobRef.value?.cancel()
        }
    }
}

// ----------------- Helper: send message to all members (NEW) -----------------
fun sendMessageToCommunity(context: Context, community: Community, message: String) {
    if (community.members.isEmpty()) {
        Toast.makeText(context, "No members in community", Toast.LENGTH_SHORT).show()
        return
    }

    val recipients = community.members.values
        .map { formatPhoneNumber(it.phone) }
        .filter { it.isNotBlank() }
        .joinToString(";")

    if (recipients.isBlank()) {
        Toast.makeText(context, "No valid phone numbers found", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        val smsUri = Uri.parse("smsto:$recipients")
        val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
            putExtra("sms_body", message)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to open SMS app", Toast.LENGTH_LONG).show()
    }
}

// ----------------- EditShareMessageDialog (NEW) -----------------
@Composable
fun EditShareMessageDialog(
    initialMessage: String,
    commName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var message by remember { mutableStateOf(initialMessage) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Confirm & Edit Message", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.height(8.dp))
                Text("Sending to community: $commName", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    label = { Text("Message to send") },
                    singleLine = false,
                    maxLines = 8
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismiss) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (message.isNotBlank()) onConfirm(message)
                        }
                    ) { Text("Send to Community") }
                }
            }
        }
    }
}

// ----------------- Data class reused in this file -----------------
data class LocationData(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val ts: Long = 0L
)
