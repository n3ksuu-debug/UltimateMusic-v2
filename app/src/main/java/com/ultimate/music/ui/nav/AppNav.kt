package com.ultimate.music.ui.nav
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.*
import com.ultimate.music.ui.screens.*
import com.ultimate.music.ui.theme.*
private data class Item(val route: String, val label: String, val icon: ImageVector)
private val items = listOf(Item("home", "Микс", Icons.Default.Home), Item("library", "Библиотека", Icons.Default.LibraryMusic), Item("settings", "Доступность", Icons.Default.Settings))
@Composable
fun AppNav() {
    val nav = rememberNavController(); val backStack by nav.currentBackStackEntryAsState(); val current = backStack?.destination?.route
    Scaffold(containerColor = VoidGreen, bottomBar = {
        if (current != "player") {
            NavigationBar(containerColor = SurfaceDark) {
                items.forEach { item ->
                    NavigationBarItem(selected = current == item.route, onClick = { nav.navigate(item.route) { popUpTo("home"); launchSingleTop = true } },
                        icon = { Icon(item.icon, item.label, tint = if (current == item.route) MoltenCopper else NeonTeal) },
                        label = { Text(item.label, color = if (current == item.route) MoltenCopper else NeonTeal) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = VoidGreen)
                    )
                }
            }
        }
    }) { pad ->
        NavHost(nav, startDestination = "home", modifier = Modifier.padding(pad)) {
            composable("home") { HomeScreen(onOpenPlayer = { nav.navigate("player") }) }
            composable("player") { PlayerScreen(onBack = { nav.popBackStack() }) }
            composable("library") { LibraryScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}