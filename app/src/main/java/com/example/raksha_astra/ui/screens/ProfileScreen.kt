package com.example.raksha_astra.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

    // --- UI State ---
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var dob by remember { mutableStateOf(TextFieldValue("")) }
    var gender by remember { mutableStateOf(TextFieldValue("Female")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var altPhone by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var workAddress by remember { mutableStateOf(TextFieldValue("")) }
    var bloodGroup by remember { mutableStateOf(TextFieldValue("")) }
    var allergies by remember { mutableStateOf(TextFieldValue("")) }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    var liveLocation by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // --- Fetch data from Firebase ---
    LaunchedEffect(Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
        ref.get().addOnSuccessListener { snapshot ->
            name = TextFieldValue(snapshot.child("name").getValue(String::class.java) ?: "")
            dob = TextFieldValue(snapshot.child("dob").getValue(String::class.java) ?: "")
            gender = TextFieldValue(snapshot.child("gender").getValue(String::class.java) ?: "")
            phone = TextFieldValue(snapshot.child("phone").getValue(String::class.java) ?: "")
            altPhone = TextFieldValue(snapshot.child("altPhone").getValue(String::class.java) ?: "")
            email = TextFieldValue(snapshot.child("email").getValue(String::class.java) ?: "")
            address = TextFieldValue(snapshot.child("address").getValue(String::class.java) ?: "")
            workAddress = TextFieldValue(snapshot.child("workAddress").getValue(String::class.java) ?: "")
            bloodGroup = TextFieldValue(snapshot.child("bloodGroup").getValue(String::class.java) ?: "")
            allergies = TextFieldValue(snapshot.child("allergies").getValue(String::class.java) ?: "")
            profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)
            liveLocation = snapshot.child("liveLocation").getValue(Boolean::class.java) ?: false
        }
    }

    // --- Image Picker ---
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedUri = uri
        uri?.let {
            scope.launch(Dispatchers.IO) {
                try {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val bytes = inputStream?.readBytes()
                    inputStream?.close()

                    if (bytes != null) {
                        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                        val body = MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", "profile.jpg", requestBody)
                            .addFormDataPart("upload_preset", "thread")
                            .build()

                        val request = Request.Builder()
                            .url("https://api.cloudinary.com/v1_1/enter cloudinary api here/image/upload")
                            .post(body)
                            .build()

                        val response = OkHttpClient().newCall(request).execute()
                        val responseStr = response.body?.string()
                        val imageUrl = Regex("\"secure_url\":\"([^\"]+)\"")
                            .find(responseStr ?: "")
                            ?.groups?.get(1)?.value

                        if (imageUrl != null) {
                            profileImageUrl = imageUrl
                            FirebaseDatabase.getInstance().getReference("users").child(uid)
                                .child("profileImageUrl").setValue(imageUrl)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // --- Save Function ---
    fun saveProfile() {
        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
        val userMap = mapOf(
            "name" to name.text,
            "dob" to dob.text,
            "gender" to gender.text,
            "phone" to phone.text,
            "altPhone" to altPhone.text,
            "email" to email.text,
            "address" to address.text,
            "workAddress" to workAddress.text,
            "bloodGroup" to bloodGroup.text,
            "allergies" to allergies.text,
            "liveLocation" to liveLocation,
            "profileImageUrl" to profileImageUrl
        )
        ref.setValue(userMap)
    }

    // --- UI Layout ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xfffc8967), Color(0xfff84e90))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Profile",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Profile image
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(profileImageUrl),
                    contentDescription = "Profile",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Add Photo", color = Color.White, fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Inputs card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileTextField("Full Name", name, Icons.Default.Person) { name = it }
                ProfileTextField("Date of Birth", dob, Icons.Default.DateRange) { dob = it }
                ProfileTextField("Gender", gender, Icons.Default.Wc) { gender = it }
                ProfileTextField("Phone Number", phone, Icons.Default.Call) { phone = it }
                ProfileTextField("Alternate Number", altPhone, Icons.Default.AddIcCall) { altPhone = it }
                ProfileTextField("Email", email, Icons.Default.Email) { email = it }
                ProfileTextField("Home Address", address, Icons.Default.Home) { address = it }
                ProfileTextField("Work/College Address", workAddress, Icons.Default.HomeWork) { workAddress = it }
                ProfileTextField("Blood Group", bloodGroup, Icons.Default.Bloodtype) { bloodGroup = it }
                ProfileTextField("Allergies/Conditions", allergies, Icons.Default.MedicalInformation) { allergies = it }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = liveLocation,
                        onCheckedChange = { liveLocation = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF3aa676))
                    )
                    Text("Enable Live Location Sharing", color = Color.White)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { saveProfile() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0c35bd)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).padding(end = 6.dp)
            ) {
                Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { /* Reset values */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFc10500)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).padding(start = 6.dp)
            ) {
                Text("Cancel", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: TextFieldValue,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onValueChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = { Icon(icon, contentDescription = null, tint = Color.Black) },
        label = { Text(label, color = Color.Black) },
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xffec5e95),
            unfocusedIndicatorColor = Color(0xff135bed),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.DarkGray
        )
    )
}





//Simple ui
//package com.example.raksha_astra.ui.screens
//
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import coil.compose.rememberAsyncImagePainter
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody.Companion.toRequestBody
//
//@Composable
//fun ProfileScreen() {
//    val context = LocalContext.current
//    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
//
//    // --- UI State ---
//    var name by remember { mutableStateOf(TextFieldValue("")) }
//    var dob by remember { mutableStateOf(TextFieldValue("")) }
//    var gender by remember { mutableStateOf(TextFieldValue("Female")) }
//    var phone by remember { mutableStateOf(TextFieldValue("")) }
//    var altPhone by remember { mutableStateOf(TextFieldValue("")) }
//    var email by remember { mutableStateOf(TextFieldValue("")) }
//    var address by remember { mutableStateOf(TextFieldValue("")) }
//    var workAddress by remember { mutableStateOf(TextFieldValue("")) }
//    var bloodGroup by remember { mutableStateOf(TextFieldValue("")) }
//    var allergies by remember { mutableStateOf(TextFieldValue("")) }
//    var profileImageUrl by remember { mutableStateOf<String?>(null) }
//    var liveLocation by remember { mutableStateOf(false) }
//
//    val scope = rememberCoroutineScope()
//
//    // --- Fetch data from Firebase ---
//    LaunchedEffect(Unit) {
//        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
//        ref.get().addOnSuccessListener { snapshot ->
//            name = TextFieldValue(snapshot.child("name").getValue(String::class.java) ?: "")
//            dob = TextFieldValue(snapshot.child("dob").getValue(String::class.java) ?: "")
//            gender = TextFieldValue(snapshot.child("gender").getValue(String::class.java) ?: "")
//            phone = TextFieldValue(snapshot.child("phone").getValue(String::class.java) ?: "")
//            altPhone = TextFieldValue(snapshot.child("altPhone").getValue(String::class.java) ?: "")
//            email = TextFieldValue(snapshot.child("email").getValue(String::class.java) ?: "")
//            address = TextFieldValue(snapshot.child("address").getValue(String::class.java) ?: "")
//            workAddress = TextFieldValue(snapshot.child("workAddress").getValue(String::class.java) ?: "")
//            bloodGroup = TextFieldValue(snapshot.child("bloodGroup").getValue(String::class.java) ?: "")
//            allergies = TextFieldValue(snapshot.child("allergies").getValue(String::class.java) ?: "")
//            profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)
//            liveLocation = snapshot.child("liveLocation").getValue(Boolean::class.java) ?: false
//        }
//    }
//
//    // --- Image Picker ---
//    var selectedUri by remember { mutableStateOf<Uri?>(null) }
//    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        selectedUri = uri
//        uri?.let {
//            scope.launch(Dispatchers.IO) {
//                try {
//                    // Upload to Cloudinary
//                    val inputStream = context.contentResolver.openInputStream(it)
//                    val bytes = inputStream?.readBytes()
//                    inputStream?.close()
//
//                    if (bytes != null) {
//                        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
//                        val body = MultipartBody.Builder()
//                            .setType(MultipartBody.FORM)
//                            .addFormDataPart("file", "profile.jpg", requestBody)
//                            .addFormDataPart("upload_preset", "thread") // 👈 from your Cloudinary settings
//                            .build()
//
//                        val request = Request.Builder()
//                            .url("https://api.cloudinary.com/v1_1/dfkfuassi/image/upload")
//                            .post(body)
//                            .build()
//
//                        val response = OkHttpClient().newCall(request).execute()
//                        val responseStr = response.body?.string()
//                        val imageUrl = Regex("\"secure_url\":\"([^\"]+)\"")
//                            .find(responseStr ?: "")
//                            ?.groups?.get(1)?.value
//
//                        if (imageUrl != null) {
//                            profileImageUrl = imageUrl
//                            FirebaseDatabase.getInstance().getReference("users").child(uid)
//                                .child("profileImageUrl").setValue(imageUrl)
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    // --- Save Function ---
//    fun saveProfile() {
//        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
//        val userMap = mapOf(
//            "name" to name.text,
//            "dob" to dob.text,
//            "gender" to gender.text,
//            "phone" to phone.text,
//            "altPhone" to altPhone.text,
//            "email" to email.text,
//            "address" to address.text,
//            "workAddress" to workAddress.text,
//            "bloodGroup" to bloodGroup.text,
//            "allergies" to allergies.text,
//            "liveLocation" to liveLocation,
//            "profileImageUrl" to profileImageUrl
//        )
//        ref.setValue(userMap)
//    }
//
//    // --- UI ---
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(Color(0xfffc8967), Color(0xfff84e90))
//                )
//            )
//            .verticalScroll(rememberScrollState())
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    )
//
//    {
//        Text(
//            "Your Profile",
//            style = MaterialTheme.typography.headlineSmall,
//            color = Color.White,
//            fontWeight = FontWeight.Bold
//        )
////        Text(
////            "Ypur Profile",
////            style = MaterialTheme.typography.headlineSmall,
////            color = Color.White
////        )
//
//        Spacer(Modifier.height(12.dp))
//        // Profile image
//        Box(
//            modifier = Modifier
//                .size(120.dp)
//                .clip(CircleShape)
//                .background(Color.Gray)
//                .clickable { launcher.launch("image/*") },
//            contentAlignment = Alignment.Center
//        ) {
//            if (profileImageUrl != null) {
//                Image(
//                    painter = rememberAsyncImagePainter(profileImageUrl),
//                    contentDescription = "Profile",
//                    modifier = Modifier.fillMaxSize()
//                )
//            } else {
//                Text("Pick Image", color = Color.White)
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        // Fields
//        ProfileTextField("Full Name", name) { name = it }
//        ProfileTextField("Date of Birth", dob) { dob = it }
//        ProfileTextField("Gender", gender) { gender = it }
//        ProfileTextField("Phone Number", phone) { phone = it }
//        ProfileTextField("Alternate Number", altPhone) { altPhone = it }
//        ProfileTextField("Email", email) { email = it }
//        ProfileTextField("Home Address", address) { address = it }
//        ProfileTextField("Work/College Address", workAddress) { workAddress = it }
//        ProfileTextField("Blood Group", bloodGroup) { bloodGroup = it }
//        ProfileTextField("Allergies/Conditions", allergies) { allergies = it }
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Checkbox(checked = liveLocation, onCheckedChange = { liveLocation = it })
//            Text("Enable Live Location Sharing", color = Color.White)
//        }
//
//        Spacer(Modifier.height(20.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(onClick = { saveProfile() }, colors = ButtonDefaults.buttonColors(Color(0xFF0c35bd))) {
//                Text("Save", color = Color.White)
//            }
//            Button(onClick = { /* maybe reset values */ }, colors = ButtonDefaults.buttonColors(Color.Gray)) {
//                Text("Cancel", color = Color.White)
//            }
//        }
//    }
//}
//
//@Composable
//fun ProfileTextField(label: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(label, color = Color(0xFF000000)) },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp),
//        colors = TextFieldDefaults.colors(
//            focusedIndicatorColor = Color(0xFF4570f3),
//            unfocusedIndicatorColor = Color.White,
//            cursorColor = Color.Blue,
//            focusedLabelColor = Color.Red,
//            unfocusedLabelColor = Color(0xFF214283),
//            focusedContainerColor = Color(0xfffeadd2),
//            unfocusedContainerColor = Color.Transparent
//        )
//    )
//}


//White Box ui
//package com.example.raksha_astra.ui.screens
//
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import coil.compose.rememberAsyncImagePainter
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody.Companion.toRequestBody
//
//@Composable
//fun ProfileScreen() {
//    val context = LocalContext.current
//    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
//
//    // --- UI State ---
//    var name by remember { mutableStateOf(TextFieldValue("")) }
//    var dob by remember { mutableStateOf(TextFieldValue("")) }
//    var gender by remember { mutableStateOf(TextFieldValue("Female")) }
//    var phone by remember { mutableStateOf(TextFieldValue("")) }
//    var altPhone by remember { mutableStateOf(TextFieldValue("")) }
//    var email by remember { mutableStateOf(TextFieldValue("")) }
//    var address by remember { mutableStateOf(TextFieldValue("")) }
//    var workAddress by remember { mutableStateOf(TextFieldValue("")) }
//    var bloodGroup by remember { mutableStateOf(TextFieldValue("")) }
//    var allergies by remember { mutableStateOf(TextFieldValue("")) }
//    var profileImageUrl by remember { mutableStateOf<String?>(null) }
//    var liveLocation by remember { mutableStateOf(false) }
//
//    val scope = rememberCoroutineScope()
//
//    // --- Fetch data from Firebase ---
//    LaunchedEffect(Unit) {
//        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
//        ref.get().addOnSuccessListener { snapshot ->
//            name = TextFieldValue(snapshot.child("name").getValue(String::class.java) ?: "")
//            dob = TextFieldValue(snapshot.child("dob").getValue(String::class.java) ?: "")
//            gender = TextFieldValue(snapshot.child("gender").getValue(String::class.java) ?: "")
//            phone = TextFieldValue(snapshot.child("phone").getValue(String::class.java) ?: "")
//            altPhone = TextFieldValue(snapshot.child("altPhone").getValue(String::class.java) ?: "")
//            email = TextFieldValue(snapshot.child("email").getValue(String::class.java) ?: "")
//            address = TextFieldValue(snapshot.child("address").getValue(String::class.java) ?: "")
//            workAddress = TextFieldValue(snapshot.child("workAddress").getValue(String::class.java) ?: "")
//            bloodGroup = TextFieldValue(snapshot.child("bloodGroup").getValue(String::class.java) ?: "")
//            allergies = TextFieldValue(snapshot.child("allergies").getValue(String::class.java) ?: "")
//            profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)
//            liveLocation = snapshot.child("liveLocation").getValue(Boolean::class.java) ?: false
//        }
//    }
//
//    // --- Image Picker ---
//    var selectedUri by remember { mutableStateOf<Uri?>(null) }
//    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        selectedUri = uri
//        uri?.let {
//            scope.launch(Dispatchers.IO) {
//                try {
//                    // Upload to Cloudinary
//                    val inputStream = context.contentResolver.openInputStream(it)
//                    val bytes = inputStream?.readBytes()
//                    inputStream?.close()
//
//                    if (bytes != null) {
//                        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
//                        val body = MultipartBody.Builder()
//                            .setType(MultipartBody.FORM)
//                            .addFormDataPart("file", "profile.jpg", requestBody)
//                            .addFormDataPart("upload_preset", "thread") // 👈 from your Cloudinary settings
//                            .build()
//
//                        val request = Request.Builder()
//                            .url("https://api.cloudinary.com/v1_1/dfkfuassi/image/upload")
//                            .post(body)
//                            .build()
//
//                        val response = OkHttpClient().newCall(request).execute()
//                        val responseStr = response.body?.string()
//                        val imageUrl = Regex("\"secure_url\":\"([^\"]+)\"")
//                            .find(responseStr ?: "")
//                            ?.groups?.get(1)?.value
//
//                        if (imageUrl != null) {
//                            profileImageUrl = imageUrl
//                            FirebaseDatabase.getInstance().getReference("users").child(uid)
//                                .child("profileImageUrl").setValue(imageUrl)
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    // --- Save Function ---
//    fun saveProfile() {
//        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
//        val userMap = mapOf(
//            "name" to name.text,
//            "dob" to dob.text,
//            "gender" to gender.text,
//            "phone" to phone.text,
//            "altPhone" to altPhone.text,
//            "email" to email.text,
//            "address" to address.text,
//            "workAddress" to workAddress.text,
//            "bloodGroup" to bloodGroup.text,
//            "allergies" to allergies.text,
//            "liveLocation" to liveLocation,
//            "profileImageUrl" to profileImageUrl
//        )
//        ref.setValue(userMap)
//    }
//
//    // --- UI ---
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(Color(0xfffc8967), Color(0xfff84e90))
//                )
//            )
//            .verticalScroll(rememberScrollState())
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            "Your Profile",
//            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
//            color = Color.White,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        // Profile image with modern styling
//        Box(
//            modifier = Modifier
//                .size(140.dp)
//                .shadow(8.dp, CircleShape, spotColor = Color(0x80FFFFFF))
//                .clip(CircleShape)
//                .background(Color.White)
//                .border(3.dp, Color.White, CircleShape)
//                .clickable { launcher.launch("image/*") },
//            contentAlignment = Alignment.Center
//        ) {
//            if (profileImageUrl != null) {
//                Image(
//                    painter = rememberAsyncImagePainter(profileImageUrl),
//                    contentDescription = "Profile",
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(CircleShape)
//                )
//            } else {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text("Add Photo", color = Color(0xFF666666), fontWeight = FontWeight.Medium)
//                    Text("Tap to upload", color = Color(0xFF888888), fontSize = MaterialTheme.typography.bodySmall.fontSize)
//                }
//            }
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        // Fields Container with modern card
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 4.dp),
//            shape = RoundedCornerShape(20.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = Color.White.copy(alpha = 0.95f)
//            ),
//            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(20.dp)
//            ) {
//                // Fields
//                ProfileTextField("Full Name", name) { name = it }
//                Spacer(Modifier.height(8.dp))
//                ProfileTextField("Date of Birth", dob) { dob = it }
//                Spacer(Modifier.height(8.dp))
//                ProfileTextField("Gender", gender) { gender = it }
//                Spacer(Modifier.height(8.dp))
//                ProfileTextField("Phone Number", phone) { phone = it }
//                Spacer(Modifier.height(8.dp))
//                ProfileTextField("Alternate Number", altPhone) { altPhone = it }
//                Spacer(Modifier.height(8.dp))
//                ProfileTextField("Email", email) { email = it }
//                Spacer(Modifier.height(8.dp))
//                ProfileTextField("Home Address", address) { address = it }
//                Spacer(Modifier.height(8.dp))
//                ProfileTextField("Work/College Address", workAddress) { workAddress = it }
//                Spacer(Modifier.height(8.dp))
//                ProfileTextField("Blood Group", bloodGroup) { bloodGroup = it }
//                Spacer(Modifier.height(8.dp))
//                ProfileTextField("Allergies/Conditions", allergies) { allergies = it }
//
//                Spacer(Modifier.height(16.dp))
//
//                // Live Location Toggle with modern styling
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0xFFF8F9FA)
//                    ),
//                    border = BorderStroke(1.dp, Color(0xFFE9ECEF))
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Checkbox(
//                            checked = liveLocation,
//                            onCheckedChange = { liveLocation = it },
//                            colors = CheckboxDefaults.colors(
//                                checkedColor = Color(0xFF0c35bd),
//                                checkmarkColor = Color.White
//                            )
//                        )
//                        Spacer(Modifier.width(8.dp))
//                        Text(
//                            "Enable Live Location Sharing",
//                            color = Color(0xFF333333),
//                            fontWeight = FontWeight.Medium,
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    }
//                }
//
//                Spacer(Modifier.height(20.dp))
//
//                // Buttons with modern styling
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    Button(
//                        onClick = { saveProfile() },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(end = 8.dp)
//                            .height(50.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFF0c35bd)
//                        ),
//                        elevation = ButtonDefaults.buttonElevation(
//                            defaultElevation = 4.dp,
//                            pressedElevation = 8.dp
//                        )
//                    ) {
//                        Text("Save", color = Color.White, fontWeight = FontWeight.SemiBold)
//                    }
//
//                    Button(
//                        onClick = { /* maybe reset values */ },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(start = 8.dp)
//                            .height(50.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFF6C757D)
//                        ),
//                        elevation = ButtonDefaults.buttonElevation(
//                            defaultElevation = 4.dp,
//                            pressedElevation = 8.dp
//                        )
//                    ) {
//                        Text("Cancel", color = Color.White, fontWeight = FontWeight.SemiBold)
//                    }
//                }
//            }
//        }
//
//        Spacer(Modifier.height(20.dp))
//    }
//}
//
//@Composable
//fun ProfileTextField(label: String, value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = {
//            Text(
//                label,
//                color = Color(0xFF666666),
//                fontWeight = FontWeight.Medium,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(2.dp, RoundedCornerShape(12.dp)),
//        shape = RoundedCornerShape(12.dp),
//        colors = TextFieldDefaults.colors(
//            focusedContainerColor = Color.White,
//            unfocusedContainerColor = Color.White,
//            focusedIndicatorColor = Color(0xFF0c35bd),
//            unfocusedIndicatorColor = Color(0xFFD1D5DB),
//            cursorColor = Color(0xFF0c35bd),
//            focusedLabelColor = Color(0xFF0c35bd),
//            unfocusedLabelColor = Color(0xFF666666),
//            focusedTextColor = Color(0xFF333333),
//            unfocusedTextColor = Color(0xFF333333)
//        ),
//        singleLine = true
//    )
//}
