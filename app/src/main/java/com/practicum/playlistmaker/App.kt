package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.Creator.Creator

class App : Application() {

    private val appThemeInteractor by lazy { Creator.provideAppThemeInteractor(this) }

    override fun onCreate() {
        super.onCreate()
        appThemeInteractor.setAppTheme()
    }
    fun switchTheme(isDarkModeEnabled: Boolean){
        appThemeInteractor.switchAppTheme(isDarkModeEnabled)
    }
}