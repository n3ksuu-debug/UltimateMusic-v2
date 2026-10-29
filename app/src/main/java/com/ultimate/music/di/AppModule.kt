package com.ultimate.music.di
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
val Context.dataStore by preferencesDataStore(name = "settings")
@Module @InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun provideContext(@ApplicationContext ctx: Context) = ctx
}