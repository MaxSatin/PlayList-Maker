package com.practicum.playlistmaker.domain.repository

interface AppThemeRepository {
    fun setDarkTheme(isDarkTheme: Boolean)
    fun checkIsDarkThemeOn(): Boolean
}
