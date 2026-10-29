package com.ultimate.music.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val Colors = darkColorScheme(
    primary = LightGreen,
    onPrimary = PureWhite,
    secondary = BgGreen,
    onSecondary = PureWhite,
    background = BgGreen,
    onBackground = PureWhite,
    surface = BlackBg,
    onSurface = PureWhite,
    outline = PureWhite,
    surfaceVariant = DarkGreen,
)

@Composable fun UltimateMusicTheme(content: @Composable () -> Unit) { 
    MaterialTheme(colorScheme = Colors, content = content) 
}
