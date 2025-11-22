package com.example.raksha_astra.ui.navigation

sealed class Screen(val route: String) {
    // 🔹 Core
    data object Splash : Screen("splash")
    data object Register : Screen("register")
    data object Login : Screen("login")
    data object Main : Screen("main")

    // 🔹 Bottom tabs
    data object Home : Screen("home")
    data object LiveShare : Screen("live_share")
    data object Community : Screen("community")
    data object Profile : Screen("profile")

    // 🔹 Drawer
    data object Contacts : Screen("contacts")
    data object WomenLaws : Screen("women_laws")
    data object SelfDefence : Screen("self_defence")
    data object Settings : Screen("settings")
    data object Helplines : Screen("helplines")
   // data object Logout : Screen("Logout")
   data object Logout : Screen("logout")


    data object Widgets : Screen("widgetsScreen")
    data object Notification : Screen("notificationScreen")
}
