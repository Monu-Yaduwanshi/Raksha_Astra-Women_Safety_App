package com.example.raksha_astra.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.raksha_astra.ui.navigation.Screen
import kotlinx.coroutines.launch
import com.example.raksha_astra.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(rootNavController: NavHostController,navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomNavController = rememberNavController()

    val bottomDestinations = listOf(
        Screen.Home to Icons.Default.Home,
        Screen.LiveShare to Icons.Default.LocationOn,
        Screen.Community to Icons.Default.Groups,
        Screen.Profile to Icons.Default.Person
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // 🔹 Drawer with solid blue background
            ModalDrawerSheet(
          //      drawerContainerColor = Color(0xFF00bfff), // solid blue background
                    drawerContainerColor = Color(0xFF4570f3), //  blue background

            )
//            ModalDrawerSheet(
//                modifier = Modifier
//                    .background(Color(0xFF4570f3))
//            )
            {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    // 🔹 App Logo (from assets)
                    Image(
                        painter = painterResource(id = R.drawable.rakshaastra), // your logo in assets
                        contentDescription = "App Logo",
                        modifier = Modifier.size(100.dp)
                    )
//                    Icon(
//                        painter = painterResource(id = R.drawable.rakshaastra),
//                        contentDescription = "App Logo",
//                        tint = Color(0xFFfc1976), // pinkish-red tint
//                        modifier = Modifier.size(100.dp)
//                    )

                    Spacer(Modifier.height(12.dp))

                    // 🔹 App Name
                    Text(
                        text = "Raksha Astra",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )

                    Spacer(Modifier.height(24.dp))
                }

                // 🔹 Drawer items
                NavigationDrawerItem(
                    label = { Text("Emergency Contacts", color = Color.White) },
                    icon = { Icon(Icons.Default.Contacts, contentDescription = "Contacts", tint = Color(0xFFfc1976)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        rootNavController.navigate(Screen.Contacts.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Women Safety Law", color = Color.White) },
                    icon = { Icon(Icons.Default.Gavel, contentDescription = "Law", tint = Color(0xFFfc1976)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        rootNavController.navigate(Screen.WomenLaws.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Self Defence Techniques", color = Color.White) },
                    icon = { Icon(Icons.Default.Security, contentDescription = "Defence", tint = Color(0xFFfc1976)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        rootNavController.navigate(Screen.SelfDefence.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Settings", color = Color.White) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color(0xFFfc1976)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        rootNavController.navigate(Screen.Settings.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Police & Helplines Numbers", color = Color.White) },
                    icon = { Icon(Icons.Default.Call, contentDescription = "Helplines", tint = Color(0xFFfc1976)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        rootNavController.navigate(Screen.Helplines.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Logout", color = Color.White) },
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color(0xFFfc1976)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        rootNavController.navigate(Screen.Logout.route) {
                            popUpTo(Screen.Main.route) { inclusive = false }
                        }
                    }
                )
            }
        }
    )

//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            // 🔹 Drawer background gradient (light blue → pink)
//            ModalDrawerSheet(
//                modifier = Modifier.background(
//                    Brush.verticalGradient(
//                        listOf(Color(0xFFADD8E6), Color(0xFFFFC0CB))
//                    )
//                )
//            ) {
//                Text(
//                    text = "Raksha Astra",
//                    style = MaterialTheme.typography.titleLarge,
//                    color = Color.White,
//                    modifier = Modifier.padding(24.dp)
//                )
//                NavigationDrawerItem(
//                    label = { Text("Emergency Contacts", color = Color.White) },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        rootNavController.navigate(Screen.Contacts.route)
//                    }
//                )
//                NavigationDrawerItem(
//                    label = { Text("Women Safety Law", color = Color.White) },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        rootNavController.navigate(Screen.WomenLaws.route)
//                    }
//                )
//                NavigationDrawerItem(
//                    label = { Text("Self Defence Techniques", color = Color.White) },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        rootNavController.navigate(Screen.SelfDefence.route)
//                    }
//                )
//                NavigationDrawerItem(
//                    label = { Text("Settings", color = Color.White) },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        rootNavController.navigate(Screen.Settings.route)
//                    }
//                )
//                NavigationDrawerItem(
//                    label = { Text("Police & Helplines Numbers", color = Color.White) },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        rootNavController.navigate(Screen.Helplines.route)
//                    }
//                )
//                NavigationDrawerItem(
//                    label = { Text("Logout", color = Color.White) },
//                    selected = false,
//                    onClick = {
//                        scope.launch { drawerState.close() }
//                        rootNavController.navigate(Screen.Logout.route) {
//                            popUpTo(Screen.Main.route) { inclusive = false }
//                        }
//                    }
//                )
//            }
//        }
//    )
    {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Raksha Astra", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFce2562) // 🔹 Pinkish top bar
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open()
                                else drawerState.close()
                            }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { rootNavController.navigate(Screen.Widgets.route) }) {
                            Icon(
                                imageVector = Icons.Default.Widgets,
                                contentDescription = "Widgets",
                                tint = Color.White
                            )
                        }

                        IconButton(onClick = { rootNavController.navigate(Screen.Notification.route) }) {
                            Icon(
                                Icons.Default.NotificationsActive,
                                contentDescription = "Notification",
                                tint = Color.White
                            )
                        }

                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF4570f3) // 🔹 Blue background

//                    containerColor = Color(0xFF4570f3) // 🔹 Blue background
                ) {
                    val backStackEntry by bottomNavController.currentBackStackEntryAsState()
                    val currentRoute = backStackEntry?.destination?.route
                    bottomDestinations.forEach { (screen, icon) ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = { bottomNavController.navigate(screen.route) },
                            icon = {
                                Icon(
                                    icon,
                                    contentDescription = screen.route,
                                    tint = if (currentRoute == screen.route) Color(0xFFce2562) else Color.White
                                )
                            },
                            label = {
                                Text(
                                    when (screen) {
                                        Screen.Home -> "Home"
                                        Screen.LiveShare -> "Location"
                                        Screen.Community -> "Community"
                                        Screen.Profile -> "Profile"
                                        else -> ""
                                    },
                                    color = if (currentRoute == screen.route) Color(0xFFce2562) else Color.White
                                )
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) { HomeScreen(rootNavController) }
                composable(Screen.LiveShare.route) { LiveLocationScreen() }
                composable(Screen.Community.route) { CommunityScreen() }
                composable(Screen.Profile.route) { ProfileScreen() }
            }
        }
    }
}

