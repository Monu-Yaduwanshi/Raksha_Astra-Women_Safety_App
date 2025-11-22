package com.example.raksha_astra.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.raksha_astra.ui.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFF80AB), Color(0xFFFE006F)) // pink gradient
                )
            )
    ) {
        // 🔹 Top black heading box with circles
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // bigger header
                .background(Color(0xFF101010))
                .padding(start = 24.dp, top = 50.dp, end = 24.dp),
            contentAlignment = Alignment.TopStart
        ) {
            // Decorative layered circles
            Canvas(modifier = Modifier.matchParentSize()) {
                // Top-right glow circle
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF1d2d3c), Color.White),
                        center = Offset(x = size.width * 0.85f, y = -100f),
                        radius = size.minDimension / 1.2f
                    ),
                    radius = size.minDimension / 1.2f,
                    center = Offset(x = size.width * 0.85f, y = -100f),
                    alpha = 0.9f
                )

                // Bottom-left soft circle
                drawCircle(
                    color = Color(0xFFe5e5e5).copy(alpha = 0.6f),
                    radius = size.minDimension / 1.7f,
                    center = Offset(x = size.width * 0.2f, y = size.height * 0.9f)
                )

                // Middle subtle gradient circle
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF0D1C2E), Color.White),
                        center = Offset(x = size.width * 0.7f, y = size.height * 0.7f),
                        radius = size.minDimension / 1.5f
                    ),
                    radius = size.minDimension / 1.5f,
                    center = Offset(x = size.width * 0.7f, y = size.height * 0.7f),
                    alpha = 0.8f
                )
            }

            // Texts on top (aligned left for Login)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                Text("Sign in to your Account", style = MaterialTheme.typography.headlineLarge, color = Color(0xff0c35bd))
               // Text("Account", style = MaterialTheme.typography.displayLarge, color = Color(0xff0c35bd))
                Spacer(Modifier.height(6.dp))
                Text("Sign in to Account", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF4570f3))
            }
        }

//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(220.dp) // ⬆️ doubled height
//                .background(Color(0xFF101010))
//                .padding(start = 24.dp, top = 50.dp, bottom = 24.dp),
//            contentAlignment = Alignment.TopStart
//        ) {
//            // Circle Decorations
//            Box(
//                modifier = Modifier
//                    .size(180.dp)
//                    .offset(x = 200.dp, y = (-40).dp)
//                    .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(90.dp))
//            )
//            Box(
//                modifier = Modifier
//                    .size(120.dp)
//                    .offset(x = 250.dp, y = 30.dp)
//                    .background(Color(0xFF2A2A2A), shape = RoundedCornerShape(60.dp))
//            )
//
//            // Heading text (aligned left)
//            Column {
//                Text(
//                    "Sign in to your Account",
//                    style = MaterialTheme.typography.headlineLarge,
//                    color = Color.White
//                )
//                Spacer(Modifier.height(6.dp))
//                Text("Sign in Account", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
//            }
//        }

        Spacer(Modifier.height(32.dp))

        // 🔹 Form Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFFE006F),
                    unfocusedIndicatorColor = Color.Blue,
                    cursorColor = Color(0xFF4570f3),
                    focusedLabelColor = Color(0xFF4570f3),         // label when focused
                    unfocusedLabelColor = Color(0xFFFE006F)              // label when not focused
                )
            )

            Spacer(Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                trailingIcon = {
                    val visibilityIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(visibilityIcon, contentDescription = "Toggle Password Visibility")
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFFE006F),
                    unfocusedIndicatorColor = Color.Blue,
                    cursorColor = Color(0xFF4570f3),
                    focusedLabelColor = Color(0xFF4570f3),         // label when focused
                    unfocusedLabelColor = Color(0xFFFE006F)              // label when not focused
                )
            )

            Spacer(Modifier.height(20.dp))

            // Login Button
            Button(
                onClick = {
                    loading = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            loading = false
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screen.Main.route) {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                            }
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4570f3)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login", color = Color.White)
            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = { navController.navigate("register") }) {
                Text("Don’t have an account? Register", color = Color.White)
            }
        }
    }
}
