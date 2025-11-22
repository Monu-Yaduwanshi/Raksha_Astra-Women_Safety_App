package com.example.raksha_astra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var autoAlert by remember { mutableStateOf(true) }
    var voiceTrigger by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFce2562))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xfffc8967), Color(0xfff84e90))))
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Preferences", style = MaterialTheme.typography.titleLarge, color = Color.White)
            Spacer(Modifier.height(16.dp))
            SwitchRow("Auto emergency alert", autoAlert) { autoAlert = it }
            SwitchRow("Voice command trigger", voiceTrigger) { voiceTrigger = it }
        }
    }
}

@Composable
private fun SwitchRow(title: String, checked: Boolean, onCheck: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White)
        Switch(checked = checked, onCheckedChange = onCheck)
    }
}
