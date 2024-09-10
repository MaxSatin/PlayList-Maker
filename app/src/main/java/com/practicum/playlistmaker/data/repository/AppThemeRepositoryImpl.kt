package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.data.storage.SharedPrefsClient
import com.practicum.playlistmaker.domain.repository.AppThemeRepository

class AppThemeRepositoryImpl(
    private val sharedPrefs: SharedPrefsClient
): AppThemeRepository {

    private val sharedPrefsAppTheme = sharedPrefs.getSharedPrefs()

    override fun setDarkTheme(isDarkTheme: Boolean) {
        sharedPrefsAppTheme.edit().putBoolean(App.IS_DARK_MODE_ON_KEY, isDarkTheme).apply()
    }

    override fun checkIsDarkThemeOn(): Boolean {
        return sharedPrefsAppTheme.getBoolean(IS_DARK_MODE_ON_KEY, false)
    }
    companion object {
        const val IS_DARK_MODE_ON_KEY = "is_dark_mode_on"
    }
}