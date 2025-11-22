package com.example.raksha_astra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush

@Composable
fun TrackingScreen(shareId: String) {
    val context = LocalContext.current
    var mapView: MapView? by remember { mutableStateOf(null) }
    var lastLocation by remember { mutableStateOf<LatLng?>(null) }
    var isExpired by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val database = FirebaseDatabase.getInstance().reference

    LaunchedEffect(shareId) {
        if (shareId.isNotEmpty()) {
            val locationListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isLoading = false
                    val expired = snapshot.child("expired").getValue(Boolean::class.java) ?: true
                    isExpired = expired

                    if (!expired) {
                        val locationData = snapshot.child("lastLocation").getValue(LocationData::class.java)
                        locationData?.let { loc ->
                            val newLatLng = LatLng(loc.lat, loc.lng)
                            lastLocation = newLatLng

                            mapView?.getMapAsync { googleMap ->
                                googleMap.clear()
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(newLatLng)
                                        .title("Live Location")
                                        .snippet("Tracking ID: $shareId")
                                )
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 16f))
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading = false
                    Toast.makeText(context, "Error tracking location", Toast.LENGTH_SHORT).show()
                }
            }

            database.child("live_shares").child(shareId).addValueEventListener(locationListener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xfffc8967), Color(0xfff84e90)))
            )
            .padding(16.dp)
    ) {
        Text(
            "Tracking Live Location",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            "Tracking ID: $shareId",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (isExpired) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.8f))
            ) {
                Text(
                    "This location sharing has expired",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Map View
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            factory = { ctx ->
                MapView(ctx).apply {
                    onCreate(null)
                    getMapAsync { googleMap ->
                        googleMap.uiSettings.isZoomControlsEnabled = true
                        googleMap.uiSettings.isMapToolbarEnabled = true
                        lastLocation?.let { location ->
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(location)
                                    .title("Tracked Location")
                            )
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
                        }
                    }
                    mapView = this
                }
            },
            update = { it.onResume() }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView?.onDestroy()
        }
    }
}

//data class LocationData(
//    val lat: Double = 0.0,
//    val lng: Double = 0.0,
//    val ts: Long = 0L
//)