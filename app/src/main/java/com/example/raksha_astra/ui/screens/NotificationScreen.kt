package com.example.raksha_astra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

// Data class for notifications
data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType,
    val isRead: Boolean = false
)

enum class NotificationType {
    SOS_ALERT, COMMUNITY_UPDATE, SECURITY_TIP, SYSTEM
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    // Sample notifications data
    val notifications = remember {
        listOf(
            NotificationItem(
                id = 1,
                title = "SOS Alert Activated",
                message = "Your SOS was triggered and your location is being shared with your emergency contacts",
                time = "2 mins ago",
                type = NotificationType.SOS_ALERT
            ),
            NotificationItem(
                id = 2,
                title = "Community Safety Update",
                message = "New safety tips available for your area. Stay alert and informed.",
                time = "1 hour ago",
                type = NotificationType.COMMUNITY_UPDATE
            ),
            NotificationItem(
                id = 3,
                title = "Security Tip",
                message = "Remember to update your emergency contacts regularly for better safety coverage",
                time = "3 hours ago",
                type = NotificationType.SECURITY_TIP
            ),
            NotificationItem(
                id = 4,
                title = "Location Sharing Active",
                message = "Your live location is being shared with Family Group",
                time = "5 hours ago",
                type = NotificationType.SYSTEM
            ),
            NotificationItem(
                id = 5,
                title = "Emergency Contact Added",
                message = "John Doe has been added to your emergency contacts",
                time = "1 day ago",
                type = NotificationType.SYSTEM,
                isRead = true
            ),
            NotificationItem(
                id = 6,
                title = "Safety Check Completed",
                message = "Your weekly safety check was completed successfully",
                time = "2 days ago",
                type = NotificationType.SYSTEM,
                isRead = true
            )
        )
    }

    var unreadCount by remember { mutableStateOf(notifications.count { !it.isRead }) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Notifications",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFce2562) // 🔥 top bar color
                ),
                actions = {
                    if (unreadCount > 0) {
                        Badge(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ) {
                            Text(
                                text = unreadCount.toString(),
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xfffc8967), Color(0xfff84e90))
                    )
                )
        ) {
            // Notifications List
            if (notifications.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "No notifications",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Notifications",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You're all caught up! New alerts will appear here.",
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationCard(
                            notification = notification,
                            onMarkAsRead = {
                                // In a real app, you'd update this in your data source
                                unreadCount = maxOf(0, unreadCount - 1)
                            }
                        )
                    }
                }
            }

            // Mark All as Read Button (if there are unread notifications)
            if (unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Button(
                        onClick = {
                            // Mark all as read logic
                            unreadCount = 0
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xfff84e90)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mark All as Read")
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onMarkAsRead: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!notification.isRead) {
                    onMarkAsRead()
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Notification Icon based on type
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when (notification.type) {
                            NotificationType.SOS_ALERT -> Color.Red.copy(alpha = 0.1f)
                            NotificationType.COMMUNITY_UPDATE -> Color.Blue.copy(alpha = 0.1f)
                            NotificationType.SECURITY_TIP -> Color.Green.copy(alpha = 0.1f)
                            NotificationType.SYSTEM -> Color.Gray.copy(alpha = 0.1f)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (notification.type) {
                        NotificationType.SOS_ALERT -> Icons.Default.Warning
                        NotificationType.COMMUNITY_UPDATE -> Icons.Default.Notifications
                        NotificationType.SECURITY_TIP -> Icons.Default.Security
                        NotificationType.SYSTEM -> Icons.Default.Notifications
                    },
                    contentDescription = null,
                    tint = when (notification.type) {
                        NotificationType.SOS_ALERT -> Color.Red
                        NotificationType.COMMUNITY_UPDATE -> Color.Blue
                        NotificationType.SECURITY_TIP -> Color.Green
                        NotificationType.SYSTEM -> Color.Gray
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Notification Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Red, shape = RoundedCornerShape(4.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = notification.time,
                    color = Color.Gray.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun NotificationBadge(count: Int) {
    if (count > 0) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(20.dp)
                .background(Color.Red, shape = RoundedCornerShape(10.dp))
        ) {
            Text(
                text = if (count > 9) "9+" else count.toString(),
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Extension function to get notification icon color
fun NotificationType.getColor(): Color {
    return when (this) {
        NotificationType.SOS_ALERT -> Color.Red
        NotificationType.COMMUNITY_UPDATE -> Color.Blue
        NotificationType.SECURITY_TIP -> Color.Green
        NotificationType.SYSTEM -> Color.Gray
    }
}

// Extension function to get notification icon
fun NotificationType.getIcon() = when (this) {
    NotificationType.SOS_ALERT -> Icons.Default.Warning
    NotificationType.COMMUNITY_UPDATE -> Icons.Default.Notifications
    NotificationType.SECURITY_TIP -> Icons.Default.Security
    NotificationType.SYSTEM -> Icons.Default.Notifications
}