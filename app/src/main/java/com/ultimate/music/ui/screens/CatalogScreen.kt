package com.ultimate.music.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.theme.*

data class CategoryCard(val title: String, val brush: Brush)
val categories = listOf(
    CategoryCard("Жанры", Brush.linearGradient(listOf(Color(0xFF00B4DB), Color(0xFF0083B0)))),
    CategoryCard("Ситуации", Brush.linearGradient(listOf(Color(0xFFB0BEC5), Color(0xFF78909C)))),
    CategoryCard("Новинки", Brush.linearGradient(listOf(Color(0xFF7CB342), Color(0xFF558B2F)))),
    CategoryCard("Хиты и чарты", Brush.linearGradient(listOf(Color(0xFFFF4081), Color(0xFFC2185B))))
)

@Composable
fun CatalogScreen() {
    val vm: PlayerViewModel = hiltViewModel()
    var query by remember { mutableStateOf("") }
    val allTracks by vm.allTracks.collectAsStateWithLifecycle()
    
    val filteredTracks = remember(query, allTracks) {
        if (query.isBlank()) emptyList()
        else allTracks.filter { 
            it.mediaMetadata.title?.contains(query, ignoreCase = true) == true || 
            it.mediaMetadata.artist?.contains(query, ignoreCase = true) == true 
        }
    }

    Column(Modifier.fillMaxSize().background(BlackBg).padding(16.dp)) {
        Text("Поиск", color = PureWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(12.dp))
        
        // Строка поиска
        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Трек, артист...", color = TabGray) },
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite,
                cursorColor = PureWhite
            ),
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TabGray) },
            singleLine = true
        )
        
        Spacer(Modifier.height(16.dp))

        if (query.isNotBlank()) {
            // Результаты поиска
            if (filteredTracks.isEmpty()) {
                Text("Ничего не найдено", color = TabGray)
            } else {
                LazyColumn {
                    items(filteredTracks) { track ->
                        Row(
                            Modifier.fillMaxWidth().clickable { vm.playTrack(track) }.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(SurfaceDark))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(track.mediaMetadata.title.toString(), color = PureWhite, fontWeight = FontWeight.Bold)
                                Text(track.mediaMetadata.artist.toString(), color = TabGray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        } else {
            // Сетка категорий (как было)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    Box(
                        Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(16.dp))
                            .background(category.brush).clickable { vm.playFlow() }.padding(16.dp)
                    ) {
                        Text(category.title, color = PureWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
