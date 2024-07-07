package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object{
    const val APP_THEME = "app_theme"
    const val IS_DARK_MODE_ON = "is_dark_mode_on"
    }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(APP_THEME, MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(IS_DARK_MODE_ON, darkTheme).apply()
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}