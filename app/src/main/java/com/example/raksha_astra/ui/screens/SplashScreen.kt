package com.example.raksha_astra.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth

// 🎨 Colors
val BlueMain = Color(0xFF4570F3)
val PinkMain = Color(0xFFFE006F)

@Composable
fun SplashScreen(onFinished: (Boolean) -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        val currentUser = FirebaseAuth.getInstance().currentUser
        onFinished(currentUser != null) // ✅ true = already logged in
    }

    // ... keep your animated background + circle


//@Composable
//fun SplashScreen(onFinished: () -> Unit) {
//    // Trigger splash navigation after delay
//    LaunchedEffect(Unit) {
//        delay(4400)
//        onFinished()
//    }

    // 🔄 Infinite wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scaleAnim"
    )
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alphaAnim"
    )

    // 🌆 Gradient Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BlueMain,
                        PinkMain
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // 🌊 Expanding WHITE rings (instead of dark colors)
        Box(
            modifier = Modifier
                .size(280.dp)
                .scale(scaleAnim)
                .alpha(alphaAnim)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.6f), Color.Blue)
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(340.dp)
                .scale(scaleAnim * 0.8f)
                .alpha(alphaAnim)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.4f), Color.Red)
                    ),
                    CircleShape
                )
        )

        // 🎯 Main Gradient Circle
        Box(
            modifier = Modifier
                .size(250.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(PinkMain, BlueMain)
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Raksha Astra",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Safety in your hands",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

