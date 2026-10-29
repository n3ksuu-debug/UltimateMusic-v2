package com.ultimate.music.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
private val Colors = darkColorScheme(primary = MoltenCopper, onPrimary = VoidGreen, secondary = NeonTeal, onSecondary = VoidGreen, background = VoidGreen, onBackground = CrystalWhite, surface = SurfaceDark, onSurface = CrystalWhite, outline = NeonTeal, surfaceVariant = SurfaceDark)
@Composable fun UltimateMusicTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme = Colors, content = content) }