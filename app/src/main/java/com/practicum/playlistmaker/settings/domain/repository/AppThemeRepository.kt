package com.practicum.playlistmaker.settings.domain.repository

interface AppThemeRepository {
    fun setDarkTheme(isDarkTheme: Boolean)
    fun checkIsDarkThemeOn(): Boolean
}
