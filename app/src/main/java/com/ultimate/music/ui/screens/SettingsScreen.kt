package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ultimate.music.ui.theme.*
@Composable
fun SettingsScreen() {
    val vm: SettingsViewModel = hiltViewModel()
    val min by vm.minimal.collectAsStateWithLifecycle(initialValue = false); val hold by vm.hold.collectAsStateWithLifecycle(initialValue = false)
    val bass by vm.bass.collectAsStateWithLifecycle(initialValue = false); val hc by vm.highContrast.collectAsStateWithLifecycle(initialValue = true)
    Column(Modifier.fillMaxSize().background(VoidGreen).padding(16.dp)) {
        Text("Доступность", color = CrystalWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(16.dp))
        SwitchRow("Минимум стимулов", min) { vm.setMinimal(it) }; SwitchRow("Удержание", hold) { vm.setHold(it) }
        SwitchRow("Визуальный бас", bass) { vm.setBass(it) }; SwitchRow("Высокий контраст", hc) { vm.setHighContrast(it) }
    }
}
@Composable private fun SwitchRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f), color = CrystalWhite, fontSize = 14.sp)
        Switch(checked, onCheckedChange = onChange, colors = SwitchDefaults.colors(checkedThumbColor = MoltenCopper, checkedTrackColor = SurfaceDark))
    }
}