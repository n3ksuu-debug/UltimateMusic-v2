package com.ultimate.music.ui.nav
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.sp // <--- ДОБАВЛЕНО
import androidx.navigation.compose.*
import com.ultimate.music.ui.components.MiniPlayer
import com.ultimate.music.ui.screens.*
import com.ultimate.music.ui.theme.*

private data class Item(val route: String, val label: String, val icon: ImageVector)
private val items = listOf(Item("home", "Главная", Icons.Default.Home), Item("library", "Коллекция", Icons.Default.LibraryMusic), Item("settings", "Настройки", Icons.Default.Settings))

@Composable
fun AppNav() {
    val nav = rememberNavController(); val backStack by nav.currentBackStackEntryAsState(); val current = backStack?.destination?.route
    Scaffold(containerColor = BgGreen, bottomBar = {
        if (current != "player") {
            Column {
                MiniPlayer(onOpenPlayer = { nav.navigate("player") })
                NavigationBar(containerColor = BlackBg) {
                    items.forEach { item ->
                        NavigationBarItem(selected = current == item.route, onClick = { nav.navigate(item.route) { popUpTo("home"); launchSingleTop = true } },
                            icon = { Icon(item.icon, item.label, tint = if (current == item.route) PureWhite else TabGray) },
                            label = { Text(item.label, color = if (current == item.route) PureWhite else TabGray, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = BgGreen)
                        )
                    }
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
