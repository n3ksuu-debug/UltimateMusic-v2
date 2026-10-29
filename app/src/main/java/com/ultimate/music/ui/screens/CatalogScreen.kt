package com.ultimate.music.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.theme.*

data class CategoryCard(val title: String, val brush: Brush)

val categories = listOf(
    CategoryCard("Жанры", Brush.linearGradient(listOf(Color(0xFF00B4DB), Color(0xFF0083B0)))),
    CategoryCard("Ситуации", Brush.linearGradient(listOf(Color(0xFFB0BEC5), Color(0xFF78909C)))),
    CategoryCard("Новинки", Brush.linearGradient(listOf(Color(0xFF7CB342), Color(0xFF558B2F)))),
    CategoryCard("Хиты и чарты", Brush.linearGradient(listOf(Color(0xFFFF4081), Color(0xFFC2185B)))),
    CategoryCard("Эпохи", Brush.linearGradient(listOf(Color(0xFF616161), Color(0xFF424242)))),
    CategoryCard("Радио", Brush.linearGradient(listOf(Color(0xFF2979FF), Color(0xFF1565C0))))
)

@Composable
fun CatalogScreen() {
    val vm: PlayerViewModel = hiltViewModel()

    Column(Modifier.fillMaxSize().background(BlackBg).padding(16.dp)) {
        Text("Музыка", color = PureWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(16.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val scale by androidx.compose.animation.core.animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(16.dp))
                        .background(category.brush)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            vm.playFlow() // Включает вашу музыку
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        category.title,
                        color = PureWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
