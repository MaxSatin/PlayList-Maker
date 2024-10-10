package com.practicum.playlistmaker.settings.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.repository.AppThemeRepository

class AppThemeRepositoryImpl(
    private val sharedPrefsAppTheme: SharedPreferences
): AppThemeRepository {

//    private val sharedPrefsAppTheme = sharedPrefs.getSharedPrefs()
    private var isDarkTheme = checkIsDarkThemeOn()

    override fun setDarkTheme(isDarkTheme: Boolean) {
        sharedPrefsAppTheme.edit().putBoolean(IS_DARK_MODE_ON_KEY, isDarkTheme).apply()
    }

    override fun checkIsDarkThemeOn(): Boolean {
        return sharedPrefsAppTheme.getBoolean(IS_DARK_MODE_ON_KEY, false)
    }

    override fun setAppTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun switchAppTheme(isDarkModeEnabled: Boolean) {
        isDarkTheme = isDarkModeEnabled
        setDarkTheme(isDarkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val IS_DARK_MODE_ON_KEY = "is_dark_mode_on"
    }
}