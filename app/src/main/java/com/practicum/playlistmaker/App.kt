package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val APP_THEME = "app_theme"
        const val IS_DARK_MODE_ON_KEY = "is_dark_mode_on"
    }

    private var isDarkTheme = false
    private val sharedPrefs by lazy {
        getSharedPreferences(APP_THEME, Context.MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        isDarkTheme = sharedPrefs.getBoolean(IS_DARK_MODE_ON_KEY, false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        sharedPrefs.edit().putBoolean(IS_DARK_MODE_ON_KEY, isDarkTheme).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}