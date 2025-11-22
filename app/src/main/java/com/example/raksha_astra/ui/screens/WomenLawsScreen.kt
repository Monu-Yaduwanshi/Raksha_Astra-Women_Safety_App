package com.example.raksha_astra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WomenLawsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Women Safety Laws", color = Color.White) },
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xfffc8967), Color(0xfff84e90))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Women Safety Laws in India",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "• IPC 354 (Assault/Criminal force...)\n" +
                        "• IPC 354A (Sexual harassment)\n" +
                        "• IPC 354D (Stalking)\n" +
                        "• IPC 509 (Insult to modesty of a woman)\n" +
                        "• POCSO Act (Child protection)\n" +
                        "• Dowry Prohibition Act\n" +
                        "• Domestic Violence Act (PWDVA)\n" +
                        "• Nirbhaya amendments & fast-track",
                color = Color.White
            )
        }
    }
}
