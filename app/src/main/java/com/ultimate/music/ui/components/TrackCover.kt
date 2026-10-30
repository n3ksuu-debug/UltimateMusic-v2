package com.ultimate.music.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ultimate.music.ui.theme.PureWhite
import kotlin.math.absoluteValue

@Composable
fun TrackCover(title: String, size: Dp = 48.dp, cornerRadius: Dp = 12.dp) {
    val colors = listOf(
        listOf(Color(0xFFFF6B6B), Color(0xFFEE5A6F)),
        listOf(Color(0xFF4ECDC4), Color(0xFF44A08D)),
        listOf(Color(0xFFFFD93D), Color(0xFFFF6B6B)),
        listOf(Color(0xFF6A11CB), Color(0xFF2575FC)),
        listOf(Color(0xFF00B4DB), Color(0xFF0083B0)),
        listOf(Color(0xFFF7971E), Color(0xFFFFD200)),
        listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0))
    )
    // Берем цвет в зависимости от хэша названия (каждый трек имеет свой цвет)
    val picked = colors[title.hashCode().absoluteValue % colors.size]
    
    Box(
        Modifier.size(size).clip(RoundedCornerShape(cornerRadius))
            .background(Brush.linearGradient(picked)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (title.isNotEmpty()) title.take(1).uppercase() else "?",
            color = PureWhite, fontSize = (size.value / 2.2).sp,
            fontWeight = FontWeight.Black
        )
    }
}
