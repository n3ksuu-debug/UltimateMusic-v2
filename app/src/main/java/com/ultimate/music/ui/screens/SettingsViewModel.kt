package com.ultimate.music.ui.screens
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ultimate.music.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(private val repo: SettingsRepository) : ViewModel() {
    val minimal = repo.minimal; val hold = repo.hold; val bass = repo.bass; val highContrast = repo.highContrast
    fun setMinimal(v: Boolean) = viewModelScope.launch { repo.setMinimal(v) }
    fun setHold(v: Boolean) = viewModelScope.launch { repo.setHold(v) }
    fun setBass(v: Boolean) = viewModelScope.launch { repo.setBass(v) }
    fun setHighContrast(v: Boolean) = viewModelScope.launch { repo.setHighContrast(v) }
}