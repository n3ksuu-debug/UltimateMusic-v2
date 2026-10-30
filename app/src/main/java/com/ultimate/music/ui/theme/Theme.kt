package com.ultimate.music.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Colors = darkColorScheme(
    primary = LightGreen, onPrimary = PureWhite, secondary = BgGreenTop, onSecondary = PureWhite,
    background = BgGreenBottom, onBackground = PureWhite, surface = SurfaceDark, onSurface = PureWhite,
    outline = PureWhite, surfaceVariant = DarkGreen,
)

// Премиальная типографика (сжатые буквы, жирные заголовки)
private val Typography = Typography(
    displayLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Black, fontSize = 32.sp, letterSpacing = (-1).sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 24.sp, letterSpacing = (-0.5).sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 16.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp)
)

@Composable fun UltimateMusicTheme(content: @Composable () -> Unit) { 
    MaterialTheme(colorScheme = Colors, typography = Typography, content = content) 
}
