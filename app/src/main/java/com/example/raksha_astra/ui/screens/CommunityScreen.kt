package com.example.raksha_astra.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.location.LocationServices

// -------- DATA CLASSES --------
//data class Community(
//    val name: String = "",
//    val members: Map<String, Member> = emptyMap(),
//    val sosMessage: String = "🚨 SOS! I'm in danger. Please help me. My location: "
//)
// Simple alternative implementation
data class Community(
    val name: String = "",
    val members: Map<String, Member> = emptyMap(),
    val sosMessage: String = "🚨 SOS! I'm in danger. Please help me. My location: "
) {
    /**
     * Simple serialization - store as name|memberCount|sosMessage
     */
    fun toJsonString(): String {
        return "$name|${members.size}|$sosMessage"
    }

    companion object {
        /**
         * Simple deserialization
         */
        fun fromJsonString(jsonString: String, communityName: String): Community {
            return try {
                val parts = jsonString.split("|")
                if (parts.size >= 3) {
                    Community(communityName, emptyMap(), parts[2])
                } else {
                    Community(communityName)
                }
            } catch (e: Exception) {
                Community(communityName)
            }
        }
    }
}

data class Member(
    val name: String = "",
    val phone: String = ""
)

// -------- Location Helper --------
fun fetchCurrentLocation(context: Context, onResult: (Location?) -> Unit) {
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    try {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedClient.lastLocation.addOnSuccessListener { location ->
                onResult(location)
            }.addOnFailureListener {
                onResult(null)
            }
        } else {
            onResult(null)
        }
    } catch (e: SecurityException) {
        onResult(null)
    }
}

@Composable
fun CommunityScreen() {
    val user = FirebaseAuth.getInstance().currentUser
    val emailKey = user?.email?.replace(".", "_") ?: "guest"
    val dbRef = FirebaseDatabase.getInstance().getReference("community").child(emailKey)

    var communities by remember { mutableStateOf<Map<String, Community>>(emptyMap()) }
    var showAddCommunityDialog by remember { mutableStateOf(false) }
    var showAddMemberDialog by remember { mutableStateOf<String?>(null) }
    var showEditSosDialog by remember { mutableStateOf<Pair<String, String>?>(null) }
    var isSendingSos by remember { mutableStateOf(false) }
    var currentSosCommunity by remember { mutableStateOf<Community?>(null) }

    val context = LocalContext.current

    // Permission launcher
    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted && currentSosCommunity != null) {
            triggerSosForCommunity(context, currentSosCommunity!!)
        } else {
            val deniedPermissions = permissions.filter { !it.value }.keys
            if (deniedPermissions.isNotEmpty()) {
                Toast.makeText(
                    context,
                    "Permission denied for: ${deniedPermissions.joinToString()}. Cannot send SOS.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        isSendingSos = false
        currentSosCommunity = null
    }

    // Load data from Firebase once
    LaunchedEffect(Unit) {
        dbRef.get().addOnSuccessListener { snapshot ->
            val loaded = snapshot.children.associate { comm ->
                val name = comm.key ?: ""
                val community = comm.getValue(Community::class.java) ?: Community(name)
                val members = community.members ?: emptyMap()
                name to community.copy(members = members)
            }
            communities = loaded
        }
    }

    // -------- UI --------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xfffc8967), Color(0xfff84e90)))
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "My Communities",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(communities.keys.toList()) { commName: String ->
                val community: Community = communities[commName] ?: return@items
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                commName,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            IconButton(onClick = {
                                dbRef.child(commName).removeValue()
                                communities = communities - commName
                                Toast.makeText(context, "Community deleted", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }

                        Spacer(Modifier.height(6.dp))

                        community.members.entries.forEach { (memberKey: String, member: Member) ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${member.name} (${formatPhoneNumber(member.phone)})", color = Color.Black)
                                IconButton(onClick = {
                                    dbRef.child(commName).child("members").child(memberKey).removeValue()
                                    val updatedMembers = community.members.toMutableMap().apply { remove(memberKey) }
                                    val updatedCommunity = community.copy(members = updatedMembers)
                                    communities = communities.toMutableMap().apply { put(commName, updatedCommunity) }
                                    Toast.makeText(context, "${member.name} removed", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove Member", tint = Color.Red)
                                }
                            }
                        }

                        Spacer(Modifier.height(6.dp))

                        Button(
                            onClick = { showAddMemberDialog = commName },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4570F3))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Add Member", color = Color.White)
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = { showEditSosDialog = commName to community.sosMessage },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4570F3))
                        ) {
                            Text("✏️ Edit SOS Message", color = Color.White)
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = {
                                if (community.members.isEmpty()) {
                                    Toast.makeText(context, "No members in community", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                debugPhoneNumbers(community)

                                isSendingSos = true
                                currentSosCommunity = community

                                val requiredPermissions = arrayOf(
                                    Manifest.permission.SEND_SMS,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )

                                val missingPermissions = requiredPermissions.filter { permission ->
                                    ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                                }

                                if (missingPermissions.isEmpty()) {
                                    triggerSosForCommunity(context, community)
                                } else {
                                    multiplePermissionsLauncher.launch(missingPermissions.toTypedArray())
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE006F)),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isSendingSos
                        ) {
                            if (isSendingSos) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Sending SOS...", color = Color.White)
                            } else {
                                Text("🚨 Send SOS to All Members", color = Color.White)
                            }
                        }

                        Text(
                            "Members: ${community.members.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        FloatingActionButton(
            onClick = { showAddCommunityDialog = true },
            containerColor = Color(0xFF4570F3),
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Community")
        }
    }

    // -------- Dialogs --------
    if (showAddCommunityDialog) {
        AddCommunityDialog(
            onDismiss = { showAddCommunityDialog = false },
            onAdd = { commName: String ->
                dbRef.child(commName).setValue(Community(commName))
                communities = communities + (commName to Community(commName))
                showAddCommunityDialog = false
                Toast.makeText(context, "Community created", Toast.LENGTH_SHORT).show()
            }
        )
    }

    showAddMemberDialog?.let { commName: String ->
        AddMemberDialog(
            onDismiss = { showAddMemberDialog = null },
            onAdd = { name: String, phone: String ->
                val formattedPhone = formatPhoneNumber(phone)
                val memberId = dbRef.child(commName).child("members").push().key
                    ?: System.currentTimeMillis().toString()
                val member = Member(name, formattedPhone)
                dbRef.child(commName).child("members").child(memberId).setValue(member)

                val oldCommunity = communities[commName]
                if (oldCommunity != null) {
                    val updatedMembers = oldCommunity.members.toMutableMap().apply { put(memberId, member) }
                    val updatedCommunity = oldCommunity.copy(members = updatedMembers)
                    communities = communities.toMutableMap().apply { put(commName, updatedCommunity) }
                }
                showAddMemberDialog = null
                Toast.makeText(context, "$name added", Toast.LENGTH_SHORT).show()
            }
        )
    }

    showEditSosDialog?.let { pair: Pair<String, String> ->
        val (commName: String, currentMessage: String) = pair
        EditSosDialog(
            currentMessage = currentMessage,
            onDismiss = { showEditSosDialog = null },
            onSave = { newMsg: String ->
                dbRef.child(commName).child("sosMessage").setValue(newMsg)
                val oldCommunity = communities[commName]
                if (oldCommunity != null) {
                    val updatedCommunity = oldCommunity.copy(sosMessage = newMsg)
                    communities = communities.toMutableMap().apply { put(commName, updatedCommunity) }
                }
                showEditSosDialog = null
                Toast.makeText(context, "SOS message updated", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// -------- Phone Number Formatting --------
fun formatPhoneNumber(phone: String): String {
    var cleaned = phone.replace("[^0-9+]".toRegex(), "")
    cleaned = cleaned.replace("^\\+91".toRegex(), "")
        .replace("^91".toRegex(), "")
        .replace("^0".toRegex(), "")
    return if (cleaned.length == 10) "+91$cleaned"
    else if (!cleaned.startsWith("+")) "+$cleaned"
    else cleaned
}

// -------- Debug Function --------
fun debugPhoneNumbers(community: Community) {
    Log.d("SOS_DEBUG", "=== Community: ${community.name} ===")
    Log.d("SOS_DEBUG", "Total members: ${community.members.size}")
    Log.d("SOS_DEBUG", "SOS Message: ${community.sosMessage}")

    community.members.values.forEachIndexed { index, member ->
        val isValidPhone = member.phone.isNotBlank() && member.phone.length >= 10
        Log.d("SOS_DEBUG", "Member ${index + 1}: ${member.name} - ${member.phone} (valid: $isValidPhone)")
    }
    Log.d("SOS_DEBUG", "=== End Debug ===")
}

// -------- SOS Sending (Modified) --------
fun triggerSosForCommunity(context: Context, community: Community) {
    if (community.members.isEmpty()) {
        Toast.makeText(context, "No members in community", Toast.LENGTH_SHORT).show()
        return
    }

    fetchCurrentLocation(context) { location: Location? ->
        val baseMessage = community.sosMessage
        val message = if (location != null) {
            "$baseMessage https://maps.google.com/?q=${location.latitude},${location.longitude}"
        } else {
            "$baseMessage [Location unavailable]"
        }

        val recipients = community.members.values
            .map { formatPhoneNumber(it.phone) }
            .filter { it.isNotBlank() }
            .joinToString(";")

        if (recipients.isBlank()) {
            Toast.makeText(context, "No valid phone numbers found", Toast.LENGTH_SHORT).show()
            return@fetchCurrentLocation
        }

        try {
            val smsUri = Uri.parse("smsto:$recipients")
            val intent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
                putExtra("sms_body", message)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Log.d("SOS", "✓ SMS intent launched with recipients: $recipients")
        } catch (e: Exception) {
            Log.e("SOS", "✗ Failed to launch SMS intent: ${e.message}")
            Toast.makeText(context, "Failed to open SMS app", Toast.LENGTH_LONG).show()
        }
    }
}

// -------- Add Community Dialog --------
@Composable
fun AddCommunityDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var communityName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Add Community", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = communityName,
                    onValueChange = { communityName = it },
                    label = { Text("Community Name") }
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismiss) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (communityName.isNotBlank()) onAdd(communityName)
                        }
                    ) { Text("Add") }
                }
            }
        }
    }
}

// -------- Add Member Dialog --------
@Composable
fun AddMemberDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var memberName by remember { mutableStateOf("") }
    var memberPhone by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Add Member", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = memberName,
                    onValueChange = { memberName = it },
                    label = { Text("Name") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = memberPhone,
                    onValueChange = { memberPhone = it },
                    label = { Text("Phone Number") }
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismiss) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (memberName.isNotBlank() && memberPhone.isNotBlank()) {
                                onAdd(memberName, memberPhone)
                            }
                        }
                    ) { Text("Add") }
                }
            }
        }
    }
}

// -------- Edit SOS Dialog --------
@Composable
fun EditSosDialog(currentMessage: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var sosMessage by remember { mutableStateOf(currentMessage) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Edit SOS Message", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = sosMessage,
                    onValueChange = { sosMessage = it },
                    label = { Text("SOS Message") }
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismiss) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (sosMessage.isNotBlank()) onSave(sosMessage)
                        }
                    ) { Text("Save") }
                }
            }
        }
    }
}


