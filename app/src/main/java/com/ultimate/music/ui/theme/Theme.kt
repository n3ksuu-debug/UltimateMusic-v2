package com.ultimate.music.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val Colors = darkColorScheme(
    primary = LightGreen, onPrimary = PureWhite, secondary = BgGreenTop, onSecondary = PureWhite,
    background = BgGreenBottom, onBackground = PureWhite, surface = SurfaceDark, onSurface = PureWhite,
    outline = PureWhite, surfaceVariant = DarkGreen,
)

@Composable fun UltimateMusicTheme(content: @Composable () -> Unit) { 
    MaterialTheme(colorScheme = Colors, content = content) 
}
