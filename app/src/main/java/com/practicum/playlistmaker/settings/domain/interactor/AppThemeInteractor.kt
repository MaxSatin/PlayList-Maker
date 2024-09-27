package com.practicum.playlistmaker.settings.domain.interactor

interface AppThemeInteractor {
    fun setAppTheme()
    fun isDarkThemeOn(): Boolean
    fun switchAppTheme(isDarkMode: Boolean)
}