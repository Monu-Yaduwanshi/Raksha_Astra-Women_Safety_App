package com.example.raksha_astra.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.raksha_astra.ui.screens.*

@Composable
fun AppNavHost(
    rootNavController: NavHostController,
    deepLinkId: String? = null,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(deepLinkId) {
        println("🔄 AppNavHost - Processing deepLinkId: $deepLinkId")

        if (!deepLinkId.isNullOrEmpty()) {
            println("🎯 AppNavHost - Navigating to tracking screen with ID: $deepLinkId")

            kotlinx.coroutines.delay(100)

            rootNavController.navigate("tracking/$deepLinkId") {
                popUpTo(0)
            }
        }
    }

    NavHost(
        navController = rootNavController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    )
    //        // 🚀 Splash Screen
    {
        composable(Screen.Splash.route) {
            println("🌅 SplashScreen composable - deepLinkId: $deepLinkId")

            SplashScreen(onFinished = { isLoggedIn ->
                println("🎯 SplashScreen finished - isLoggedIn: $isLoggedIn, deepLinkId: $deepLinkId")

                if (!deepLinkId.isNullOrEmpty()) {
                    println("📍 Navigating to tracking screen from splash: tracking/$deepLinkId")
                    rootNavController.navigate("tracking/$deepLinkId") {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                } else {
                    println("🚶 Using normal navigation flow")
                    rootNavController.navigate(
                        if (isLoggedIn) Screen.Main.route else Screen.Register.route
                    ) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            })
        }

        // 📝 Register Screen
        composable(Screen.Register.route) {
            RegisterScreen(rootNavController)
        }



        // 🔑 Login Screen
        composable(Screen.Login.route) {
            LoginScreen(rootNavController)
        }


        // notification and widgets
// 🧩 Widgets Screen
        composable(Screen.Widgets.route) {
            WidgetsScreen(rootNavController)
        }

        // notification
        composable(Screen.Notification.route) {
            NotificationScreen(rootNavController)
        }

        // 🏠 Main Scaffold (Bottom + Drawer navigation)
        // 🏠 Main Scaffold (Bottom + Drawer navigation)
        composable(Screen.Main.route) {
            val mainNavController = rememberNavController()
            MainScaffold(rootNavController = rootNavController, navController = mainNavController)
        }

//        composable(Screen.Main.route) {
//            MainScaffold(rootNavController = rootNavController)
//        }

        // ✅ Bottom Bar Screens
        composable(Screen.Home.route) { HomeScreen(rootNavController) }
        composable(Screen.LiveShare.route) { LiveLocationScreen() }
        composable(Screen.Community.route) { CommunityScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }

        // ✅ Drawer Screens
        composable(Screen.Contacts.route) { ContactsScreen(rootNavController) }
        composable(Screen.WomenLaws.route) { WomenLawsScreen(rootNavController) }
        composable(Screen.SelfDefence.route) { SelfDefenceScreen(rootNavController) }
        composable(Screen.Settings.route) { SettingsScreen(rootNavController) }
        composable(Screen.Helplines.route) { HelplinesScreen(rootNavController) }
        composable(Screen.Logout.route) { LogoutScreen(rootNavController) }
// Update the tracking screen composable:
        composable(
            route = "tracking/{shareId}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "rakshaastra://track/{shareId}"
                },
                navDeepLink {
                    uriPattern = "https://rakshaastra.page.link/track/{shareId}"
                },
                navDeepLink {
                    uriPattern = "http://rakshaastra.page.link/track/{shareId}"
                }
            )
        ) { backStackEntry ->
            val shareId = backStackEntry.arguments?.getString("shareId") ?: ""
            println("🗺️ TrackingScreen opened with shareId: $shareId")
            TrackingScreen(shareId = shareId)
        }
//        composable(
//            route = "tracking/{shareId}",
//            deepLinks = listOf(
//                navDeepLink {
//                    uriPattern = "rakshaastra://track/{shareId}"
//                },
//                navDeepLink {
//                    uriPattern = "https://rakshaastra.com/track/{shareId}"
//                }
//            )
//        ) { backStackEntry ->
//            val shareId = backStackEntry.arguments?.getString("shareId") ?: ""
//            println("🗺️ TrackingScreen opened with shareId: $shareId")
//            TrackingScreen(shareId = shareId)
//        }
    }
}