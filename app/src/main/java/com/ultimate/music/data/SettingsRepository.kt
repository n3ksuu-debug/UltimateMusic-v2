package com.ultimate.music.data
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.ultimate.music.di.dataStore

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext private val ctx: Context) {
    private object Keys { 
        val MIN = booleanPreferencesKey("min"); val HOLD = booleanPreferencesKey("hold"); val BASS = booleanPreferencesKey("bass"); val HC = booleanPreferencesKey("hc")
        val LIKED = stringSetPreferencesKey("liked_tracks") // <-- НОВОЕ
    }
    val minimal = ctx.dataStore.data.map { it[Keys.MIN] ?: false }
    val hold = ctx.dataStore.data.map { it[Keys.HOLD] ?: false }
    val bass = ctx.dataStore.data.map { it[Keys.BASS] ?: false }
    val highContrast = ctx.dataStore.data.map { it[Keys.HC] ?: true }
    
    // Список лайкнутых треков (хранит их ID/URI)
    val likedTracks = ctx.dataStore.data.map { it[Keys.LIKED] ?: emptySet() }

    suspend fun setMinimal(v: Boolean) { ctx.dataStore.edit { it[Keys.MIN] = v } }
    suspend fun setHold(v: Boolean) { ctx.dataStore.edit { it[Keys.HOLD] = v } }
    suspend fun setBass(v: Boolean) { ctx.dataStore.edit { it[Keys.BASS] = v } }
    suspend fun setHighContrast(v: Boolean) { ctx.dataStore.edit { it[Keys.HC] = v } }
    
    // Логика лайка
    suspend fun toggleLike(trackId: String) {
        ctx.dataStore.edit { prefs ->
            val current = prefs[Keys.LIKED] ?: emptySet()
            if (current.contains(trackId)) prefs[Keys.LIKED] = current - trackId
            else prefs[Keys.LIKED] = current + trackId
        }
    }
}
